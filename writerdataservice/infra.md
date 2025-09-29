
# 🐳 Infraestrutura Local – Desafio Kafka + MongoDB

Este guia mostra como subir, parar e acessar a infraestrutura necessária para o desafio técnico (MongoDB + Mongo Express + Kafka + Kafka UI) usando **Docker Compose** no WSL.

---

## 🚀 Subindo a infra

Dentro da pasta `~/infra-desafio`:

```bash
# subir em background
docker compose up -d
```

Ver containers ativos:

```bash
docker ps
```

---

## ⏹️ Parando a infra



```bash
docker compose down
```

---

## 📂 Serviços disponíveis

### 1. **MongoDB**

* **Host**: `localhost`
* **Porta**: `27017`
* **Database default para o projeto**: `desafio`
* **String de conexão (Spring Boot)**:

  ```
  spring.data.mongodb.uri=mongodb://localhost:27017/desafio
  ```

🔧 Como usar:

* A aplicação Java conecta direto nessa URI.
* Para acessar via shell dentro do container:

  ```bash
  docker exec -it desafio-mongo mongosh
  ```
* Comandos básicos no `mongosh`:

  ```mongodb
  show dbs
  use desafio
  show collections
  db.usuarios.find()
  ```

---

### 2. **Mongo Express (UI para MongoDB)**

* **URL**: [http://localhost:8081](http://localhost:8081)
* **Usuário**: `admin`
* **Senha**: `admin`

🔧 Como usar:

* Login com `admin/admin`.
* Navegar pelas bases e coleções criadas pela aplicação.
* Inserir, editar e deletar documentos manualmente (apenas para teste/dev).

---

### 3. **Zookeeper**

* **Porta**: `2181`
* Usado apenas como dependência do Kafka.
* Você **não precisa interagir** diretamente com ele.

---

### 4. **Kafka (Broker)**

* **Broker**: `localhost:9092`

🔧 Como usar:

* Ainda não será usado pelo primeiro microsserviço (API Insert).
* Será usado depois pelo Writer/Search.
* Teste de tópicos (apenas curiosidade):

  ```bash
  docker exec -it desafio-kafka kafka-topics --bootstrap-server localhost:9092 --list
  ```

---

### 5. **Kafka UI (Interface web para Kafka)**

* **URL**: [http://localhost:8080](http://localhost:8080)

🔧 Como usar:

* Painel web para visualizar brokers, tópicos e mensagens.
* Você poderá ver quando os serviços Writer/Search começarem a produzir/consumir mensagens.

---

## 📝 Fluxo esperado do projeto

1. **API Insert** (Java, local via IntelliJ) → conecta no Mongo (`localhost:27017`) e salva dados em `usuarios`.
2. **Mongo Express** → permite você visualizar em tempo real os documentos inseridos.
3. **Kafka/Kafka UI** → ficam prontos para a segunda fase (outros microsserviços).

---

## ⚠️ Notas importantes

* **Persistência**: o volume `mongo_data` garante que os dados no Mongo sobrevivam mesmo com `docker compose down`.
* **Senhas fracas**: `admin/admin` é só para desenvolvimento.
* **Ordem de inicialização**: Mongo deve subir antes da aplicação Java, mas o Docker Compose já cuida disso.

---

## 🏁 Ciclo rápido de trabalho

1. Subir a infra:

   ```bash
   docker compose up -d
   ```
2. Rodar a aplicação no IntelliJ (Java → Spring Boot).
3. Testar endpoints da API com `curl` ou Postman.
4. Conferir coleções no [Mongo Express](http://localhost:8081).
5. (Mais tarde) Monitorar mensagens no [Kafka UI](http://localhost:8080).

