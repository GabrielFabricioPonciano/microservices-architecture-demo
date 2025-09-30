@echo off
echo ========================================
echo  PARANDO MICROSERVICOS
echo ========================================

echo.
echo Parando todos os containers...
docker-compose down

echo.
echo Removendo containers orfaos...
docker-compose down --remove-orphans

echo.
echo ========================================
echo  AMBIENTE PARADO COM SUCESSO!
echo ========================================
echo.
pause