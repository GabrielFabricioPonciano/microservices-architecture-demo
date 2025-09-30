# GUIA RÁPIDO - MICROSERVIÇOS

## 🚀 Para WSL/Linux (RECOMENDADO)

```bash
# 1. Copiar para WSL
cp -r /mnt/d/Users/Gabriel/Desktop/microservices ~/microservices
cd ~/microservices

# 2. Dar permissões
chmod +x start.sh stop.sh apiservice/mvnw searchprocesservice/mvnw writerdataservice/mvnw

# 3. Subir tudo
./start.sh

# 4. Testar
curl http://localhost:8080/v1/status
```

## 🪟 Para Windows (Sem Docker)

```batch
# 1. Compilar JARs
start.bat

# 2. Subir infraestrutura (no WSL)
docker-compose -f docker-compose-infra.yml up -d

# 3. Executar microserviços (3 terminais)
cd apiservice && java -jar target/apiservice-0.0.1-SNAPSHOT.jar
cd searchprocesservice && java -jar target/searchprocesservice-0.0.1-SNAPSHOT.jar  
cd writerdataservice && java -jar target/writerdataservice-0.0.1-SNAPSHOT.jar
```

## 📊 URLs Importantes

- **API REST**: http://localhost:8080
- **Swagger**: http://localhost:8080/swagger-ui.html  
- **Mongo Express**: http://localhost:8081 (admin/admin)
- **Kafka UI**: http://localhost:8082

## 🛑 Para Parar

```bash
# WSL/Linux
./stop.sh

# Windows
docker-compose down
```