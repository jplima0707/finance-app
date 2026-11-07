# Decisões, alternativas e aprendizados

Documento de consulta pessoal. Para cada tema: **o problema**, **o que escolhi**, **as alternativas** e **o que aprendi**.

> Legenda: ✅ já implementado no projeto · 🔜 planejado / ainda não implementado.

## Sumário

1. [Transactional Outbox](#1-transactional-outbox-)
2. [Relay: polling vs CDC vs bibliotecas](#2-relay-polling-vs-cdc-vs-bibliotecas-)
3. [Tópicos Kafka: partições e réplicas](#3-tópicos-kafka-partições-e-réplicas-)
4. [Idempotência, eventId e trace_id](#4-idempotência-eventid-e-trace_id-)
5. [Locks otimista e pessimista](#5-locks-otimista-e-pessimista-)
6. [Armadilhas que já apareceram](#6-armadilhas-que-já-apareceram-)
7. [Pendências conhecidas](#7-pendências-conhecidas)

---

## 1. Transactional Outbox ✅

### O problema (dual write)

Ao criar uma transação, o serviço precisa fazer duas coisas: **gravar no banco** e **publicar um evento no Kafka**. Banco e Kafka não compartilham transação, então qualquer ordem pode dar errado:

| Ordem | Falha | Resultado |
|---|---|---|
| Publica e depois grava | O commit do banco falha | Evento publicado de algo que **não existe** |
| Grava e depois publica | O processo cai ou o Kafka está fora | Transação existe, mas **ninguém foi avisado** |

Antes do outbox, o código publicava **dentro** do `@Transactional`, antes do commit, ou seja, caía no primeiro caso.

### Solução

Gravar o evento numa tabela do **próprio banco**, **na mesma transação** da entidade. Como é uma transação só, ou os dois existem ou nenhum existe. Um processo separado (o *relay*) lê a tabela e entrega ao Kafka.

```
POST /transactions
  └─ BEGIN
       INSERT transactions
       INSERT outbox_events (PENDING)     ← mesma transação
     COMMIT
        ...
  Relay (a cada 5s):
       SELECT PENDING ... FOR UPDATE SKIP LOCKED
       envia ao Kafka → marca SENT
```

### Tabela central ou uma por entidade?

**Escolhi: tabela central `outbox_events`**, com `aggregate_type` e `aggregate_id`.

- ✅ Contas e usuários futuros reaproveitam a mesma tabela, o mesmo relay e o mesmo código, sem nova migration.
- ✅ Um único ponto para monitorar eventos pendentes e com falha.
- ⚠️ Uma tabela mais movimentada. Mitigação: índice em `(status, id)` e rotina de limpeza das linhas `SENT`.
- Alternativa (uma tabela por entidade): isolamento maior, mas multiplica tabela, repositório e relay para cada agregado.

### Decisões do desenho

- **Payload serializado na hora da escrita.** O relay só lê e envia. Assim o evento reflete o estado no momento da transação, não o do momento do envio.
- **`event_id` da linha = `eventId` do payload.** Gerado antes de gravar. Permite que o consumer deduplique.
- **`Propagation.MANDATORY` no `OutboxService.append`.** Falha se não houver transação ativa. Impede escrever no outbox "solto", fora da transação da entidade.
- **Ordem por `id` sequencial** (`BIGINT GENERATED ALWAYS AS IDENTITY`), não por `created_at`. Eventos da mesma transação têm timestamps praticamente iguais; o identity dá ordem estável.

### Garantia de entrega: at-least-once

O Kafka pode confirmar o envio e o processo cair **antes** de o relay gravar `SENT`. Na próxima rodada a linha continua `PENDING` e o evento sai **de novo**. Por isso a entrega é *at-least-once* (pelo menos uma vez), nunca *exactly-once*. **Consumers precisam ser idempotentes** (deduplicar por `eventId` ou `transactionId`).

---

## 2. Relay: polling vs CDC vs bibliotecas ✅

Relay é um **padrão**. É o componente que move linhas da tabela outbox para o broker.

### Opções

| Opção | Como funciona | Prós | Contras |
|---|---|---|---|
| **Polling** (escolhida) | Job agendado faz `SELECT` nas linhas pendentes e envia | Simples, sem infra extra, fácil de entender e depurar | Latência de ~1s, uma query por ciclo, mantém lock durante o envio |
| **CDC / log tailing** (ex.: Debezium) | Lê o WAL do Postgres e publica as mudanças | Latência baixa, sem polling, sem lock longo, não consome CPU do app | Mais infra (Kafka Connect, config do Postgres com replicação lógica), mais difícil de operar |
| **Biblioteca pronta** (ex.: Spring Modulith, Debezium embarcado) | Implementa o padrão por você | Menos código próprio | Acopla ao framework, esconde o mecanismo — ruim para aprender |

### Por que polling agora

- Este projeto é um laboratório: quero **entender o mecanismo**, e não escondê-lo atrás de uma biblioteca.
- O volume é baixo, então a latência de ~1s e o custo do polling são irrelevantes.
- Trocar por CDC depois é **localizado**: a escrita no outbox não muda, só o consumo da tabela.

### Como o scheduling funciona por baixo

- `@EnableScheduling` registra um `ScheduledAnnotationBeanPostProcessor`, que encontra os métodos `@Scheduled` e os registra num `TaskScheduler`.
- O scheduler chama o método do bean **proxied**, por isso o `@Transactional` funciona.
- **`fixedDelay` ≠ `fixedRate`.** `fixedDelay=1000` espera 1s *depois* que a execução anterior termina, então nunca há duas execuções sobrepostas. `fixedRate` dispararia a cada 1s mesmo com lote lento, empilhando execuções.
- O `TaskScheduler` padrão do Spring Boot tem **pool de 1 thread**: os `@Scheduled` rodam em série. Se houver mais jobs, ajustar `spring.task.scheduling.pool.size`.

### Pontos de cuidado do relay

- **A transação fica aberta durante o envio ao Kafka**, com as linhas travadas. `batch-size` e `send-timeout-ms` limitam o tempo máximo do lock.
- **`FOR UPDATE SKIP LOCKED`**: várias instâncias do serviço podem rodar o relay ao mesmo tempo, e cada uma pega linhas diferentes. É um lock pessimista usado de propósito (ver seção 5).
- **Parar no primeiro erro do lote**, para um evento posterior não ultrapassar um anterior que falhou. O evento só sai do caminho ao virar `FAILED` depois de `max-attempts`. Um evento que sempre falha atrasa a fila, então vale monitorar `attempts` e `last_error`.

---

## 3. Tópicos Kafka: partições e réplicas ✅

Configuração atual (`KafkaTopicConfig`): cada tópico com **3 partições** e **1 réplica**.

```java
TopicBuilder.name(KafkaTopics.TRANSACTION_CREATED)
        .partitions(3)
        .replicas(1)
        .build();
```

### Partição

Um tópico é dividido em **partições**: logs ordenados, só de acréscimo (*append-only*). Cada mensagem vai para exatamente uma partição.

- **Ordem só é garantida dentro de uma partição**, nunca entre partições.
- **Como a partição é escolhida:** se a mensagem tem **chave**, `hash(chave) % nº de partições`. A mesma chave cai sempre na mesma partição.
- No projeto, a chave é o **`sourceAccountId`**. Resultado: todos os eventos da mesma conta de origem ficam na mesma partição e **mantêm a ordem relativa**. Eventos de contas diferentes podem andar em paralelo.
- **Paralelismo:** dentro de um *consumer group*, cada partição é lida por **no máximo um** consumer. Com 3 partições, o máximo útil é 3 consumers do mesmo grupo; um quarto ficaria ocioso.

Regras práticas:

- Dá para **aumentar** partições depois, mas **não diminuir**.
- Aumentar partições **muda o mapeamento chave → partição**, o que pode quebrar a ordem para chaves já em uso. Escolher bem no começo.
- Mais partições = mais paralelismo, mas também mais arquivos, mais overhead no broker e rebalanceamentos mais lentos.

### Réplica (replication factor)

Cada partição é copiada em `N` brokers. Uma cópia é o **líder** (recebe leituras e escritas), as demais são **followers** que a acompanham. Se o broker do líder cai, um follower sincronizado assume.

- **`replicas(1)`** = só existe o líder: **sem tolerância a falhas**. É aceitável aqui porque o ambiente local tem **um único broker**, e o fator de réplica não pode ser maior que o número de brokers.
- Em produção o comum é **3 réplicas**, com `min.insync.replicas=2` e o produtor com `acks=all`. Assim uma escrita só é confirmada quando pelo menos 2 cópias a têm, e o cluster tolera a queda de 1 broker sem perder dado confirmado.

### Termos que aparecem junto

- **Consumer group:** conjunto de consumers que dividem as partições de um tópico entre si. O Kafka guarda o *offset* (posição de leitura) por grupo e partição. O `group-id` do serviço está em `application.yaml`.
- **ISR (in-sync replicas):** réplicas que estão em dia com o líder. É o conjunto que pode virar líder sem perda.
- **`acks`:** `0` não espera confirmação, `1` espera só o líder, `all` espera todas as ISR. Para dinheiro, `all`.

---

## 4. Idempotência, eventId e trace_id ✅

Três identificadores, três papéis diferentes:

| Campo | Papel | Onde vive |
|---|---|---|
| `idempotencyKey` | Evita criar a mesma transação duas vezes quando o **cliente** repete o request | `transactions.idempotency_key` (único), vem do request |
| `eventId` | Identifica **um evento** para o consumer deduplicar | Payload + `outbox_events.event_id` |
| `traceId` | Correlaciona tudo que pertence ao **mesmo request** (logs e eventos) | `EventMetadata.traceId` |

- **Idempotência no request:** `findByIdempotencyKey` antes de criar. Se já existe, devolve a existente com **200** (e não 201) e **não publica evento de novo**. A constraint `UNIQUE` no banco é a garantia final contra duas requisições simultâneas.
- **`idempotencyKey` no metadata do evento** serve para **correlação**, não para deduplicação. Quem consome deduplica por `eventId` (ou por `transactionId`, que é único).
- **`traceId` gerado no serviço** (UUID por request), por enquanto. Quando entrar OpenTelemetry, deve vir do contexto/header (`traceparent`) em vez de ser gerado aqui.
- `EventMetadata` ganhou os campos mantendo o construtor antigo de 4 argumentos, para não quebrar quem já criava metadata sem eles.

---

## 5. Locks otimista e pessimista 🔜

> Ainda não implementados para saldo/conta. São o próximo problema real: duas operações concorrentes sobre a mesma conta. O único lock pessimista hoje é o `FOR UPDATE SKIP LOCKED` do relay.

### O problema (lost update)

Duas transações leem o saldo `100`. Uma debita 30 e grava `70`; a outra debita 50 e grava `50`. O resultado devia ser `20`, mas ficou `50`: uma atualização foi perdida.

### Lock otimista

Assume que o conflito é **raro**. Não trava nada na leitura; **detecta** o conflito na hora de gravar.

- Adiciona-se uma coluna `version`; no JPA, `@Version`.
- O update vira `UPDATE ... SET ..., version = version + 1 WHERE id = ? AND version = ?`.
- Se outra transação mudou a linha antes, nenhuma linha é atualizada e o JPA lança `OptimisticLockException`. A operação deve ser **repetida** (releitura + nova tentativa).

| Prós | Contras |
|---|---|
| Sem locks no banco, escala bem | Sob **alta contenção**, muitas tentativas descartadas |
| Sem risco de deadlock | Precisa de lógica de retry |
| Ótimo para linhas pouco disputadas | O conflito só aparece no final do trabalho |

### Lock pessimista

Assume que o conflito é **provável**. **Trava a linha logo na leitura**, e quem chegar depois espera.

- No SQL: `SELECT ... FOR UPDATE`. No JPA: `@Lock(LockModeType.PESSIMISTIC_WRITE)`.
- A segunda transação **bloqueia** até a primeira dar commit ou rollback.

| Prós | Contras |
|---|---|
| Sem retries; serializa o acesso | Transações esperam umas pelas outras: menor throughput |
| Previsível sob alta contenção | **Risco de deadlock** |
| | Locks seguram conexões do pool |

### Deadlock

Transferência A→B trava A e depois B; transferência B→A trava B e depois A. Cada uma espera a outra: deadlock. **Mitigação: sempre travar na mesma ordem**, por exemplo pelo menor `accountId` primeiro, independentemente de quem é origem e destino.

### Qual usar onde (hipótese a validar)

- **Saldo de conta** (muita escrita concorrente, dinheiro em jogo): tendência a **pessimista**, ou otimista com retry bem controlado. Medir antes de decidir.
- **Cadastro de usuário, status de conta** (conflito raro): **otimista**.
- **Fila de trabalho** (como o relay): pessimista com `SKIP LOCKED`, pois o objetivo é justamente que cada worker pegue linhas diferentes.

### Variante: `SKIP LOCKED` e `NOWAIT`

- `SKIP LOCKED`: ignora linhas travadas por outros (usado no relay).
- `NOWAIT`: falha imediatamente se a linha estiver travada, em vez de esperar.

---

## 6. Pendências conhecidas

- [ ] Testar o caminho de falha do relay (Kafka fora do ar, retries, `FAILED`).
- [ ] Rotina de retenção das linhas `SENT` em `outbox_events`.
- [ ] Passar `AccountTransactionPublisher` (accepted/rejected) pelo outbox quando for usado.
- [ ] Renomear `TransactionPublisher`, que agora só escreve no outbox (ex.: `TransactionEventPublisher`).
- [ ] Idempotência no **consumer** (deduplicar por `eventId`).
- [ ] Lock otimista/pessimista no saldo da conta.
- [ ] Trocar o `traceId` gerado localmente pelo contexto do OpenTelemetry.
- [ ] Rever fator de réplica, `acks` e `min.insync.replicas` ao ir para um cluster real.
