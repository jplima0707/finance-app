# 💰 Financial Microservices Playground

Este projeto é um **laboratório técnico pessoal** para explorar os desafios de construir sistemas financeiros distribuídos, com foco em **transações, consistência, confiabilidade, observabilidade e tolerância a falhas**.

O objetivo não é apenas fazer a aplicação funcionar, mas entender as decisões e trade-offs envolvidos em sistemas que lidam com dinheiro, concorrência, retries, mensagens duplicadas e falhas parciais.

A arquitetura será construída de forma incremental, mantendo inicialmente apenas os componentes necessários para explorar esses problemas.

---

# 🛠️ Tecnologias

As tecnologias são escolhidas para representar ferramentas relevantes no desenvolvimento de sistemas distribuídos modernos e, ao mesmo tempo, permitir explorar diferentes abordagens.

- [x] **Java** — linguagem principal
- [x] **Spring Boot** — framework base dos serviços
- [x] **Apache Kafka** — mensageria e arquitetura orientada a eventos
- [x] **Spring Kafka** — integração dos serviços com Kafka
- [x] **PostgreSQL** — persistência relacional e operações transacionais
- [ ] **NoSQL** — exploração de modelos orientados a acesso e alta escala
- [x] **Docker / Docker Compose** — ambiente local
- [ ] **Testcontainers** — testes de integração com infraestrutura real
- [ ] **OpenTelemetry** — logs, métricas e distributed tracing
- [ ] **Prometheus** — métricas
- [ ] **Grafana** — visualização e dashboards
- [ ] **Loki** — centralização de logs
- [ ] **Tempo** — distributed tracing
- [ ] **Kubernetes** — orquestração
- [ ] **Kind** — Kubernetes local
- [x] **Maven** — build e gerenciamento de dependências

---

# ▶️ Como rodar o projeto

## Pré-requisitos

- Docker
- Docker Compose
- Java 17+
- Maven

## 1️⃣ Subir os serviços

Na raiz do projeto:

```bash
docker compose up --build -d
```

Para verificar os containers em execução:

```bash
docker compose ps
```

Para acompanhar os logs:

```bash
docker compose logs -f
```

Para parar o ambiente:

```bash
docker compose down
```

## 2️⃣ Kafka UI

A interface do Kafka estará disponível em:

```text
http://localhost:8090
```

## 3️⃣ Swagger

Após os serviços iniciarem, as APIs estarão disponíveis através do Swagger:

### Accounting Service

```text
http://localhost:8081/swagger-ui/index.html
```

### Ledger Service

```text
http://localhost:8082/swagger-ui/index.html
```

> As portas e endpoints podem ser alterados conforme a configuração dos serviços.

---

# 🎯 Goals

- [ ] Construir um fluxo completo de transações financeiras entre contas
- [x] Separar claramente as responsabilidades de **Accounting** e **Ledger**
- [x] Utilizar **Kafka** para comunicação assíncrona baseada em eventos
- [ ] Garantir idempotência em requisições externas e no processamento de eventos
- [ ] Explorar **at-least-once delivery**, retries e mensagens duplicadas
- [ ] Implementar **Transactional Outbox** e **Inbox Pattern**
- [ ] Garantir consistência entre persistência e publicação de eventos
- [ ] Explorar concorrência e condições de corrida em operações sobre saldo
- [ ] Implementar mecanismos de retry e **Dead Letter Queue (DLQ)**
- [ ] Versionar contratos de eventos
- [ ] Implementar **distributed tracing** e métricas com OpenTelemetry
- [ ] Rastrear uma transação ponta a ponta através dos serviços e eventos Kafka
- [ ] Criar testes de integração com **Testcontainers**
- [ ] Simular falhas de banco, Kafka e consumidores
- [ ] Explorar diferentes estratégias de persistência para o Ledger
- [ ] Executar os serviços em um cluster Kubernetes local com **Kind**
- [ ] Documentar decisões arquiteturais e seus trade-offs
- [ ] Explorar segurança básica entre serviços e proteção das APIs
- [ ] Implementar health checks e graceful shutdown
- [ ] Avaliar limites de consistência, disponibilidade e recuperação do sistema

---

# 🧩 Serviços

## 📒 Accounting Service

Responsável pela **operação financeira do ponto de vista do negócio**:

- receber solicitações de transação;
- criar e manter o lifecycle da transação;
- controlar estados como `PENDING`, `COMPLETED` e `REJECTED`;
- publicar e consumir eventos relacionados à transação;
- garantir idempotência das requisições externas.

**Não é dono do saldo.**

## 📚 Ledger Service

Responsável pela **verdade financeira do sistema**:

- verificar saldo;
- efetivar débitos e créditos;
- garantir consistência das movimentações;
- controlar concorrência;
- impedir efeitos financeiros duplicados;
- registrar as entradas do ledger.

**É a autoridade sobre o estado financeiro efetivamente registrado.**

## 📡 Kafka

Backbone de eventos entre os serviços:

- comunicação assíncrona;
- eventos de domínio;
- retries e reprocessamento;
- desacoplamento entre serviços.

## 📊 Observability Stack

Responsável por permitir acompanhar o comportamento do sistema distribuído:

- OpenTelemetry;
- métricas;
- logs;
- distributed tracing;
- dashboards e análise de falhas.

---

# 🔐 Confiabilidade e consistência

O projeto irá explorar principalmente:

- [ ] Idempotency Key
- [ ] Idempotent Consumers
- [ ] Event ID e Transaction ID
- [ ] Inbox Pattern
- [ ] Transactional Outbox
- [ ] Retry
- [ ] Dead Letter Queue
- [ ] At-least-once delivery
- [ ] Concorrência e isolamento
- [ ] Atomicidade das operações financeiras
- [ ] Recuperação após falhas
- [ ] Reconciliação entre estados
- [ ] Event versioning

---

# 🧪 Testes

- [ ] Testes unitários
- [ ] Testes de integração
- [ ] Testcontainers
- [ ] Kafka em ambiente de teste
- [ ] Banco real em ambiente de teste
- [ ] Testes de fluxo end-to-end
- [ ] Testes de mensagens duplicadas
- [ ] Testes de retries
- [ ] Testes de concorrência
- [ ] Testes de falhas e recuperação

---

# ☸️ Infraestrutura

- [ ] Docker Compose
- [ ] Health checks
- [ ] Graceful shutdown
- [ ] Kubernetes com Kind
- [ ] Deployments
- [ ] Services
- [ ] ConfigMaps
- [ ] Secrets
- [ ] Resource requests e limits

---

# 🎯 Goals finais

Ao final, o projeto deverá demonstrar, na prática, como um sistema financeiro distribuído pode manter suas invariantes diante de:

- concorrência;
- retries;
- mensagens duplicadas;
- falhas parciais;
- indisponibilidade de dependências;
- inconsistência entre serviços;
- reprocessamento de eventos.

A intenção é terminar o projeto entendendo não apenas **como implementar** uma transação financeira distribuída, mas **por que determinadas garantias e padrões são necessários**.

---

# 🏗️ Arquitetura final idealizada

A arquitetura abaixo representa uma possível evolução do projeto. **Ela não define o escopo inicial nem significa que todos os serviços serão implementados.** Novos componentes devem ser adicionados somente quando existir uma responsabilidade que justifique sua separação.

```text
                         ┌─────────────────────┐
                         │   Client / API      │
                         └──────────┬──────────┘
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │     Accounting      │
                         │       Service       │
                         └──────────┬──────────┘
                                    │
                           TransactionRequested
                                    │
                                    ▼
                                  Kafka
                                    │
             ┌──────────────────────┼──────────────────────┐
             │                      │                      │
             ▼                      ▼                      ▼
     ┌───────────────┐      ┌───────────────┐      ┌───────────────┐
     │  Anti-Fraud   │      │    Ledger     │      │ Notification  │
     │    Service    │      │    Service    │      │    Service    │
     └───────┬───────┘      └───────┬───────┘      └───────────────┘
             │                      │
             └──────────┬───────────┘
                        ▼
                       Kafka
                        │
             ┌──────────┴──────────┐
             ▼                     ▼
      ┌──────────────┐      ┌──────────────┐
      │  Accounting  │      │   Analytics  │
      │ / Reporting  │      │    Service   │
      └──────────────┘      └──────────────┘


        ┌──────────────────────────────────────────┐
        │            Observability                 │
        │                                          │
        │ OpenTelemetry → Prometheus / Grafana     │
        │                → Loki / Tempo             │
        └──────────────────────────────────────────┘
```

Essa arquitetura futura serve como **direção de aprendizado**, não como contrato rígido do projeto. O objetivo é permitir que a arquitetura evolua conforme os problemas encontrados durante a implementação.
