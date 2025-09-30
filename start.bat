@echo off
echo ========================================
echo  COMPILANDO MICROSERVICOS (WINDOWS)
echo ========================================

echo.
echo [1/3] Compilando APIService...
cd apiservice
call mvnw.cmd clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo ERRO: Falha ao compilar APIService
    pause
    exit /b 1
)
cd ..

echo.
echo [2/3] Compilando SearchProcesserService...
cd searchprocesservice
call mvnw.cmd clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo ERRO: Falha ao compilar SearchProcesserService
    pause
    exit /b 1
)
cd ..

echo.
echo [3/3] Compilando WriterDataService...
cd writerdataservice
call mvnw.cmd clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo ERRO: Falha ao compilar WriterDataService
    pause
    exit /b 1
)
cd ..

echo.
echo ========================================
echo  COMPILACAO CONCLUIDA COM SUCESSO!
echo ========================================
echo.
echo NOTA: Este script apenas compila os JARs.
echo Para subir o ambiente completo, use WSL/Linux com Docker.
echo.
echo Para executar os microservicos localmente:
echo.
echo 1. Subir infraestrutura:
echo    Use Docker Desktop ou WSL para rodar:
echo    docker-compose -f docker-compose-infra.yml up -d
echo.
echo 2. Executar microservicos (em terminais separados):
echo    cd apiservice ^&^& java -jar target/apiservice-0.0.1-SNAPSHOT.jar
echo    cd searchprocesservice ^&^& java -jar target/searchprocesservice-0.0.1-SNAPSHOT.jar  
echo    cd writerdataservice ^&^& java -jar target/writerdataservice-0.0.1-SNAPSHOT.jar
echo.
pause