

## Tecnologias obrigatórias

- **Java 8** – linguagem dos três microsserviços
- **Spring Boot** – API REST e configuração dos serviços
- **Apache Kafka** – mensageria entre serviços
- **MongoDB** – armazenamento não relacional (entrada e saída)

------

## Ferramentas complementares (mínimas)

- **Maven** – build/dependências
- **Spring Data MongoDB** – integração com Mongo
- **Spring Kafka** – produtor/consumidor Kafka
- **Docker** *(opcional, recomendado)* – subir Kafka e Mongo rapidamente
- **Lombok** *(opcional)* – reduzir boilerplate

------

## O que **não** usar

- Kubernetes (use Docker Compose se precisar)
- Frameworks extras de validação (faça validação básica manual)
- Outras filas (ex.: RabbitMQ)
- Versões modernas de Java (fique no 8)

------

## Objetivo

Construir um sistema que **lê e processa arquivos TXT** de pessoas, gravando no MongoDB e permitindo consulta do **status** e do **resultado** (idade calculada).

**Processamento significa:**

1. Ler cada linha do TXT
2. Extrair **nome**, **CPF** (com/sem máscara) e **data de nascimento**
3. **Inserir em `usuarios` apenas se o CPF ainda não existir**, com `status="em processamento"`
4. A cada 5 minutos, calcular a **idade**
5. Publicar `{cpf, idade}` no Kafka
6. O Writer grava/atualiza a idade em `idades` e marca `usuarios.status="finalizado"`
7. Expor consultas simples por API

------

## Entrada (TXT)

Formato por linha:

```
NOME - DOCUMENTO - DATA_NASCIMENTO
```

**Exemplo:**

```
THIAGO - 25830128004 - 05/05/1990
```

> O CPF pode ter máscara (`258.301.280-04`) ou não (`25830128004`).

------

## Saída (consulta)

Consulta por **CPF** e listagem geral:

```
CPF - IDADE
```

**Exemplo:**

```
25830128004 - 34
```

------

## Arquitetura de microsserviços

### 1) API Insert (REST)

- Recebe **upload** do TXT (um arquivo)
- **Normaliza** CPF, **valida** data
- **Insere em `usuarios` somente se o CPF não existir**
  - Documento mínimo: `{cpf, nome, data_nascimento, status:"em processamento"}`
- Fornece **consultas** (status e resultados)

### 2) Search Process Service (job a cada 5 min)

- Lê `usuarios` com `status="em processamento"`

- **Calcula idade** (data atual − data_nascimento)

- **Publica** no Kafka (`Q_PROC_FIM`) **uma mensagem por CPF**:

  ```json
  { "cpf": "25830128004", "idade": 34 }
  ```

- **Somente leitura** no Mongo (não escreve)

### 3) Writer Data Service (consumidor Kafka)

- Consome `Q_PROC_FIM`
- **Idempotência**: faz **upsert** em `idades` por `cpf` → `{cpf, idade}`
- Atualiza `usuarios` por `cpf` → `status="finalizado"`
- *Commit* do offset **após** gravar em `idades` **e** atualizar `usuarios`

------

## Kafka (mínimo e direto)

- **Tópico:** `Q_PROC_FIM`
- **Key:** `cpf` (garante ordem por CPF e ajuda na idempotência)
- **Garantia:** *at-least-once* (pode haver mensagens repetidas) → o **upsert** mantém estado final correto

------

## APIs (5 definidas)

### 1) Upload do TXT

`POST /v1/usuarios/file` *(multipart/form-data; campo `file`)*

- Lê linhas, normaliza e valida
- **Se CPF não existe:** insere `{cpf, nome, data_nascimento, status:"em processamento"}`
- **Se CPF já existe:** **ignora** a linha
- **Resposta (202):**

```json
{
  "linhas_lidas": 100,
  "inseridas": 72,
  "ignoradas_por_cpf_existente": 28,
  "erros": 0
}
```

------

### 2) Status por CPF único

```
GET /v1/status/{cpf}
```

- **200 OK**

```json
{ "cpf": "25830128004", "status": "finalizado" }
```

- **404** se não cadastrado

------

### 3) Status geral (todos)

```
GET /v1/status
```

- **200 OK**

```json
[
  { "nome":"thiago" , "cpf":   "25830128004", "status": "finalizado" },
  { "nome":"gabriel", "cpf":   "12345678910", "status": "finalizado" },
  { "nome":"ana"    , "cpf":   "21345678910", "status": "em processamento" }
]
```

- **200 OK** com array vazio se não houver usuários

------

### 4) Resultados (todos)

```
GET /v1/idades
```

- **200 OK**

```json
[
  { "cpf": "25830128004", "idade": 34 },
  { "cpf": "11122233344", "idade": 37 }
]
```

------

### 5) Resultado por CPF

```
GET /v1/idades/{cpf}
```

- **200 OK**

```json
{ "cpf": "25830128004", "idade": 34 }
```

- **202 Accepted** se `usuarios.status="em processamento"`
- **404** se CPF não existir

------

## Modelo de dados (MongoDB) — coleções e exemplos

### Coleção: `usuarios`

Documento **mínimo**:

```json
{
  "_id": "usaremos LONGID",
  "cpf": "25830128004",
  "nome": "THIAGO",
  "data_nascimento": "1990-05-05",
  "status": "em processamento"
}
```

**Índices recomendados:**

- `unique(cpf)`  ← impede duplicidade de usuário
- `status`       ← acelera busca do job

------

### Coleção: `idades`

Documento **mínimo**:

```json
{
  "_id": "LONGID",
  "cpf": "25830128004",
  "idade": 34
}
```

**Índice recomendado:**

- `unique(cpf)`  ← 1 resultado por CPF

> Observação objetiva: mantemos **duas coleções** por exigência do cliente (“entrada e saída separados”). É mais simples juntar tudo? **Sim**. Mas **não** vamos contra o requisito.

------

## Fluxo textual (resumo operativo)

```
[Cliente] -> POST /v1/usuarios/file (TXT)
  API: normaliza/valida -> INSERE em `usuarios` se CPF não existir (status="em processamento")

[5 em 5 min] Search Process:
  Lê `usuarios` com status="em processamento" -> calcula idade -> envia Kafka {cpf, idade}

Writer:
  Consome Kafka -> UPSERT `idades` por cpf -> UPDATE `usuarios` por cpf (status="finalizado") -> ACK
```

------

## Riscos e contramedidas

- **Mensagens duplicadas no Kafka:** tratadas via **upsert por CPF** em `idades`
- **Status “finalizado” antes de idade gravada:** evitado pela **ordem** (primeiro upsert, depois status) e *ack* só no final
- **Reenvio de CPF para corrigir dados:** **não permitido** por decisão sua (insert-only). Se mudar depois, troque inserção por **upsert** em `usuarios`

------

