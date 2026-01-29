# 💰 Financial Microservices Playground

Este projeto é um **laboratório técnico pessoal** criado com o objetivo de **entender melhor os desafios técnicos do setor financeiro** e **praticar tecnologias e padrões arquiteturais amplamente utilizados em fintechs e bancos digitais**.

O foco não é apenas “fazer funcionar”, mas **entender por que as coisas são feitas de determinada forma** em sistemas que lidam com dinheiro, consistência, auditoria e escalabilidade.

> ⚠️ **Importante:**  
> Este projeto utiliza **microserviços** e **arquitetura orientada a eventos (Event-Driven Architecture)** de forma propositalmente mais complexa do que o necessário para um projeto simples.  
> Isso foi uma decisão consciente de *over engineering* com fins educacionais.

---

# 🎯 Objetivos do Projeto

- Compreender desafios técnicos do domínio financeiro  
- Praticar **arquitetura de microserviços**  
- Entender **event-driven architecture com Kafka**  
- Trabalhar conceitos como:
  - consistência eventual  
  - idempotência  
  - rastreabilidade de eventos  
  - separação de responsabilidades  
- Criar uma base sólida para evoluções futuras (ledger, extratos, auditoria, etc.)

---

# 🧱 Arquitetura Geral

A arquitetura segue o padrão:

- **REST** para comandos síncronos (request/response)
- **Kafka** para propagação de eventos entre serviços
- **Bancos isolados por serviço**
- Comunicação desacoplada entre domínios

Visão simplificada:

Frontend
|
API Gateway (futuro)
|
Transaction Service (REST)
|
Kafka (events)
|
Accounting Service
|
(futuro) Statement / Ledger / Reports


---

# 🧩 Serviços e Responsabilidades

## 📒 Accounting Service (Spring Boot + PostgreSQL)

Responsável por:
- Gerenciamento de contas (Account)
- Manutenção do saldo atual
- Status da conta (ativa, bloqueada, suspensa, etc.)
- Operações de leitura e consulta

⚠️ **Não atualiza saldo diretamente via API.**  
O saldo é atualizado exclusivamente a partir de **eventos de transação**.

---

## 💸 Transaction Service (Spring Boot + PostgreSQL)

Responsável por:
- Criação de transações financeiras
- Registro de débitos e créditos
- Validação de regras básicas do domínio
- Publicação de eventos no Kafka (`TransactionCreated`, etc.)

⚠️ **Não é dono do saldo.**  
Ele apenas registra fatos financeiros e emite eventos.

---

## 📡 Kafka (Event Backbone)

Responsável por:
- Comunicação assíncrona entre serviços
- Propagação de eventos de domínio
- Base para consistência eventual
- Possibilitar replay, auditoria e novos consumidores

---

# 🛠️ Tecnologias Utilizadas

- Java 17+
- Spring Boot
- Spring Data JPA
- Spring Kafka
- Apache Kafka
- PostgreSQL
- Docker & Docker Compose
- Maven

---

# ▶️ Como rodar o projeto localmente

## Pré-requisitos

- Docker  
- Docker Compose  
- Java 17+  
- Maven  

---

## 1️⃣ Subir o projeto

Na raiz do projeto:

```bash
docker-compose up --build -d
```

Isso irá subir:

 - Zookeeper

 - Kafka

 - Accouting Service

 - Transaction Service

 - Kafka UI

 - PostgreSQL (um banco por serviço)

A interface do Kafka UI ficará disponível em:

http://localhost:8090

A Swagger Api interface ficará desponível em:

http://localhost:8081/swagger-ui/index.html (accounting-service)

http://localhost:8082/swagger-ui/index.html (transaction-service)

---

# 📝 TODO / Roadmap

## 🔧 Qualidade e robustez

- [ ] Criar **testes unitários** nos serviços  
- [ ] Criar **testes de integração** (Kafka + banco)  
- [x] Definir e discutir **Idempotency Key** no transaction-service  
- [ ] Implementar **Idempotency Key** no transaction-service  
- [ ] Implementar **Outbox Pattern** para garantir consistência entre banco e Kafka  
- [ ] Implementar **retry e DLQ** para consumidores Kafka  

---

## 🧮 Modelagem de domínio

- [x] Criar **Accounting Service**  
- [x] Criar **Transaction Service**  
- [x] Modelar entidades principais (`Account`, `Transaction`)  
- [x] Utilizar `BigDecimal` para valores monetários  
- [ ] Criar tipos especiais:
  - [ ] CPF  
  - [ ] CNPJ  
  - [ ] Telefone  
  - [ ] Email  
- [ ] Criar um **Value Object `Money`**:
  - [ ] valor  
  - [ ] moeda (BRL, USD, etc.)  
  - [ ] regras de precisão  

---

## 📡 Event-driven / Mensageria

- [x] Subir **Kafka local** com Docker Compose  
- [x] Configurar **Spring Kafka** nos serviços  
- [x] Criar tópicos Kafka iniciais  
- [x] Publicar eventos a partir do transaction-service  
- [x] Consumir eventos no accounting-service  
- [ ] Versionar eventos de domínio  
- [ ] Implementar **idempotência no consumer**  

---

## 📊 Novos serviços

- [ ] Criar **Statement Service** (extratos) utilizando MongoDB  
- [ ] Criar **Ledger Service** (registro contábil imutável)  
- [ ] Criar **Report Service** (relatórios financeiros e métricas)  
- [ ] Criar **Notification Service** (eventos e alertas)

---

## ☁️ Infraestrutura

- [x] Criar infraestrutura local com **Docker Compose**  
- [ ] Migrar infraestrutura para **Kubernetes local (Kind)**  
- [ ] Criar manifests Kubernetes (Deployment, Service, ConfigMap)  
- [ ] Explorar observabilidade (logs, métricas, tracing)  

---

## 🔗 Explorações avançadas

- [ ] Criar um **Blockchain Adapter**  
- [ ] Estudar integração entre eventos financeiros e blockchain  
- [ ] Avaliar possíveis casos de uso (auditoria, imutabilidade, provas)


# 🧠 Observação final

Este projeto não busca simplicidade, e sim aprendizado profundo.
Muitas decisões foram tomadas propositalmente para simular cenários reais do setor financeiro, mesmo que isso represente mais complexidade do que o necessário para um projeto simples.

O objetivo é sair daqui entendendo como sistemas financeiros realmente funcionam por dentro.


