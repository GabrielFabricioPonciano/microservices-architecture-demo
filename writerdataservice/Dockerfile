# Usar uma imagem base com OpenJDK 8
FROM openjdk:8-jdk-alpine

# Definir diretório de trabalho
WORKDIR /app

# Copiar o arquivo JAR gerado pelo Maven
COPY target/writerdataservice-0.0.1-SNAPSHOT.jar app.jar

# Expor a porta da aplicação
EXPOSE 8082

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]