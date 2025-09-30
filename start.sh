#!/bin/bash

echo "========================================"
echo "  COMPILANDO E INICIANDO MICROSERVICOS"
echo "========================================"

echo ""
echo "[1/4] Compilando APIService..."
cd apiservice
./mvnw clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "ERRO: Falha ao compilar APIService"
    exit 1
fi
cd ..

echo ""
echo "[2/4] Compilando SearchProcesserService..."
cd searchprocesservice
./mvnw clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "ERRO: Falha ao compilar SearchProcesserService"
    exit 1
fi
cd ..

echo ""
echo "[3/4] Compilando WriterDataService..."
cd writerdataservice
./mvnw clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "ERRO: Falha ao compilar WriterDataService"
    exit 1
fi
cd ..

echo ""
echo "[4/4] Iniciando ambiente Docker..."
docker-compose down
docker-compose up --build -d

echo ""
echo "========================================"
echo "  AMBIENTE INICIADO COM SUCESSO!"
echo "========================================"
echo ""
echo "Servicos disponiveis:"
echo "- API Service:       http://localhost:8080"
echo "- Swagger UI:        http://localhost:8080/swagger-ui.html"
echo "- SearchProcessor:   http://localhost:8083"
echo "- WriterData:        http://localhost:8084"
echo "- Mongo Express:     http://localhost:8081 (admin/admin)"
echo "- Kafka UI:          http://localhost:8082"
echo ""
echo "Para parar os servicos: docker-compose down"
echo "Para ver logs: docker-compose logs -f [nome-do-servico]"
echo ""