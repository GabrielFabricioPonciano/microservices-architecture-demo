# Microsserviços - Processamento de Usuários

Sistema de 3 microsserviços em Java/Spring Boot com Kafka e MongoDB - **AMBIENTE COMPLETAMENTE DOCKERIZADO**.

## 🚀 Como Executar

### **No WSL/Linux (RECOMENDADO)**

#### Opção 1: Script Automático
```bash
# Dar permissão de execução
chmod +x start.sh stop.sh

# Compila tudo e sobe o ambiente completo
./start.sh
```

#### Opção 2: Manual no WSL
```bash
# 1. Compilar todos os JARs
cd apiservice && ./mvnw clean package -DskipTests && cd ..
cd searchprocesservice && ./mvnw clean package -DskipTests && cd ..
cd writerdataservice && ./mvnw clean package -DskipTests && cd ..

# 2. Subir ambiente completo
docker-compose up --build -d
```

### **No Windows (Sem Docker)**

#### Opção 1: Script Automático
```batch
# Apenas compila os JARs (não sobe Docker)
start.bat
```

#### Opção 2: Manual no Windows
```batch
# Compilar cada microserviço
cd apiservice && call mvnw.cmd clean package -DskipTests && cd ..
cd searchprocesservice && call mvnw.cmd clean package -DskipTests && cd ..
cd writerdataservice && call mvnw.cmd clean package -DskipTests && cd ..

# Subir infraestrutura separadamente (MongoDB e Kafka)
# Usar arquivo "docker-compose.yml" apenas com infra
```

### Para parar tudo:
```bash
# WSL/Linux
./stop.sh

# Ou manual
docker-compose down
```

## 🌐 Serviços Disponíveis

| Serviço | URL | Descrição |
|---------|-----|-----------|
| **API Service** | http://localhost:8080 | API REST principal |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | Documentação interativa |
| **SearchProcessor** | http://localhost:8083 | Processamento (interno) |
| **WriterData** | http://localhost:8084 | Escrita (interno) |
| **Mongo Express** | http://localhost:8081 | Interface MongoDB (admin/admin) |
| **Kafka UI** | http://localhost:8082 | Interface Kafka |

## � Instruções para WSL

### 1. Copiar projeto para WSL
```bash
# No WSL, navegar para uma pasta (ex: /home/usuario/)
cp -r /mnt/d/Users/Gabriel/Desktop/microservices ~/microservices
cd ~/microservices
```

### 2. Dar permissões de execução
```bash
chmod +x start.sh stop.sh
chmod +x apiservice/mvnw
chmod +x searchprocesservice/mvnw  
chmod +x writerdataservice/mvnw
```

### 3. Executar
```bash
# Ambiente completo (infraestrutura + microserviços)
./start.sh

# Ou apenas infraestrutura
docker-compose -f docker-compose-infra.yml up -d
```

### 4. Verificar se está funcionando
```bash
# Ver containers rodando
docker ps

# Ver logs
docker-compose logs -f

# Testar API
curl http://localhost:8080/v1/status
```

## 📝 Testar o Sistema

### 1. Upload de arquivo
```bash
# Criar arquivo usuarios.txt com formato: NOME - CPF - DD/MM/AAAA
echo "João Silva - 12345678901 - 15/05/1990" > usuarios.txt

# Upload
curl -X POST "http://localhost:8080/v1/usuarios/file" \
  -F "file=@usuarios.txt"
```

### 2. Consultar status
```bash
curl "http://localhost:8080/v1/status/12345678901"
```

### 3. Aguardar job (5 min) e consultar idade
```bash
curl "http://localhost:8080/v1/idades/12345678901"
```

## 📁 Estrutura

```
microservices/
├── README.md                    # Este arquivo
├── docker-compose.yml          # MongoDB + Kafka
├── .gitignore                  
├── apiservice/                 # REST API (porta 8080)
├── searchprocesservice/        # Job scheduler (porta 8081)
└── writerdataservice/          # Kafka consumer (porta 8082)
```

## 🗃️ Banco de Dados

**MongoDB**: localhost:27017
- Coleção `usuarios`: {nome, cpf, dataNascimento, status}  
- Coleção `idades`: {cpf, idade}

## 🔄 Fluxo

1. Upload arquivo → salva usuários com status PROCESSAMENTO
2. Job (5min) → calcula idade → publica no Kafka  
3. Consumer → salva idade → status FINALIZADO

## 🛠️ Stack

- Java 8 + Spring Boot + Maven
- Apache Kafka  
- MongoDB
- Docker Compose

##  Infraestrutura

### Subindo a infraestrutura
```bash
# Subir em background
docker-compose up -d

# Ver containers ativos
docker ps
```

### Parando a infraestrutura
```bash
docker-compose down
```

### Serviços disponíveis

#### 1. MongoDB
- **Host**: `localhost:27017`
- **Database**: `desafio`
- **String de conexão**: `mongodb://localhost:27017/desafio`

**Acesso via shell:**
```bash
docker exec -it desafio-mongo mongosh
```

**Comandos básicos:**
```mongodb
show dbs
use desafio
show collections
db.usuarios.find()
```

#### 2. Mongo Express (UI para MongoDB)
- **URL**: http://localhost:8081
- **Usuário**: `admin`
- **Senha**: `admin`

#### 3. Apache Kafka
- **Broker**: `localhost:9092`
- **Tópico principal**: `Q_PROC_FIM`

**Listar tópicos:**
```bash
docker exec -it desafio-kafka kafka-topics --bootstrap-server localhost:9092 --list
```

#### 4. Kafka UI (Interface web)
- **URL**: http://localhost:8080
- Visualizar brokers, tópicos e mensagens

#### 5. Zookeeper
- **Porta**: `2181`
- Usado como dependência do Kafka

## Como Executar

### 1. Subir infraestrutura
```bash
docker-compose up -d
```

### 2. Acessar serviços
- **API Swagger**: http://localhost:8080/swagger-ui.html
- **Mongo Express**: http://localhost:8081 (admin/admin)
- **Kafka UI**: http://localhost:8080

## 📁 Estrutura do Projeto

```
microservices/
├── README.md                    # Documentação unificada
├── docker-compose.yml          # Infraestrutura (MongoDB, Kafka, UIs)
├── .gitignore                  # Configuração Git
├── apiservice/                 # Microsserviço de API REST
│   ├── src/main/java/br/com/desafio/apiservice/
│   │   ├── application/
│   │   │   ├── controller/     # StatusController, UploadController, ResultController
│   │   │   ├── dto/           # StatusResponse, UploadResultResponse, IdadeResponse
│   │   │   └── handler/       # GlobalExceptionHandler
│   │   ├── config/            # OpenApiConfig
│   │   ├── domain/
│   │   │   ├── entity/        # UsuarioDocument, IdadeDocument, UsuarioStatus
│   │   │   ├── repository/    # UsuarioRepository, IdadeRepository
│   │   │   └── service/       # UploadService, StatusService, ResultadoService
│   │   ├── exception/         # UsuarioNaoEncontradoException
│   │   └── util/              # CpfUtil, FileProcessorUtil
│   └── pom.xml
├── searchprocesservice/        # Microsserviço de processamento
│   ├── src/main/java/br/com/desafio/searchprocesservice/
│   │   ├── config/            # KafkaProducerConfig
│   │   ├── domain/
│   │   │   ├── entity/        # UsuarioDocument
│   │   │   ├── repository/    # UsuarioRepository
│   │   │   └── service/       # ProcessamentoUsuarioService, KafkaProducerService
│   │   └── job/               # UsuarioJobScheduler
│   └── pom.xml
└── writerdataservice/          # Microsserviço de escrita
    ├── src/main/java/br/com/desafio/writerdataservice/
    │   ├── config/            # KafkaConsumerConfig
    │   ├── domain/
    │   │   ├── entity/        # IdadeDocument, UsuarioDocument
    │   │   ├── repository/    # IdadeRepository, UsuarioRepository
    │   │   └── service/       # ConsumerServiceKafka, InsertIdadeMongo
    │   └── dto/               # UsuarioIdadeMessage
    └── pom.xml
```

## ⚙️ Configurações

### application.properties - API Service (8080)
```properties
server.port=8080
spring.data.mongodb.uri=mongodb://localhost:27017/desafio
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### application.properties - Search Process Service (8081)
```properties
server.port=8081
spring.data.mongodb.uri=mongodb://localhost:27017/desafio
spring.kafka.producer.bootstrap-servers=localhost:9092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer


desafio.scheduler.delay=300000
```

### application.properties - Writer Data Service (8082)
```properties
server.port=8082
spring.data.mongodb.uri=mongodb://localhost:27017/desafio
spring.kafka.consumer.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=writer-group
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.properties.spring.json.trusted.packages=*
```

## APIs Disponíveis

### API Service (porta 8080)

#### Upload de Arquivo
```http
POST /v1/usuarios/file
Content-Type: multipart/form-data

Formato do arquivo: NOME - CPF - DD/MM/AAAA
```

**Resposta (202 Accepted):**

```json
{
  "lidas": 100,
  "inseridas": 95,
  "cpfsJaExistentes": 3,
  "errosDeFormato": 2,
  "arquivo": "usuarios.txt"
}
```

#### Consultar Status
```http
GET /v1/status/{cpf}
GET /v1/status?usuarioStatus=PROCESSAMENTO
```

**Resposta:**
```json
{
  "nome": "João Silva Santos",
  "cpf": "12345678901",
  "dataNascimento": "1990-05-15",
  "status": "PROCESSAMENTO",
  "idade": null
}
```

#### Consultar Idades
```http
GET /v1/idades
GET /v1/idades/{cpf}
```

**Resposta:**
```json
{
  "cpf": "12345678901",
  "idade": 33
}
```

**Status Codes:**
- `200`: Idade encontrada
- `202`: Em processamento  
- `404`: Usuário não encontrado

## 🔄 Fluxo de Processamento

1. **Upload**: Cliente envia arquivo via API
2. **Validação**: Sistema valida formato, CPFs e datas
3. **Persistência**: Usuários salvos com status "PROCESSAMENTO"
4. **Job**: A cada 5 minutos, busca usuários pendentes
5. **Cálculo**: Calcula idade baseada na data de nascimento
6. **Kafka**: Publica `{cpf, idade}` no tópico `Q_PROC_FIM`
7. **Consumer**: Recebe mensagem e faz upsert na coleção `idades`
8. **Finalização**: Status alterado para "FINALIZADO"

## 📝 Formato do Arquivo

```
João Silva Santos - 12345678901 - 15/05/1990
Maria Oliveira - 123.456.789-01 - 25/12/1985
Pedro Costa - 11122233344 - 01/01/2000
```

**Regras:**
- **Separador**: ` - ` (espaço-hífen-espaço)
- **CPF**: com ou sem máscara (11122233344 ou 111.222.333-44)
- **Data**: DD/MM/AAAA (formato brasileiro)
- **Encoding**: UTF-8
- **Nome**: texto livre (será convertido para MAIÚSCULAS)

**Validações aplicadas:**
- CPF com dígitos verificadores válidos
- Data deve existir (30/02/2023 é rejeitado)
- Linhas em branco são ignoradas
- CPFs duplicados no arquivo são aceitos (último prevalece)
- CPFs já existentes no banco são ignorados

