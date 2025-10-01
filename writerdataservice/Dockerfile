# --- Estágio 1: Build (Compila o código Java) ---
# Usamos uma imagem oficial do Maven que já contém o JDK 8.
FROM maven:3.8-openjdk-8 AS builder

# Define o diretório de trabalho dentro do container de build.
WORKDIR /app

# Copia o pom.xml primeiro para otimizar o cache do Docker.
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o resto do código fonte.
COPY src ./src

# Executa o build do Maven para gerar o .jar.
RUN mvn clean package -DskipTests


# --- Estágio 2: Final (Executa a aplicação) ---
# Usamos uma imagem leve, apenas com o Java Runtime.
FROM openjdk:8-jre-slim

# Define o diretório de trabalho.
WORKDIR /app

# Copia APENAS o .jar do estágio 'builder'.
# O wildcard (*) garante que funciona mesmo se a versão mudar.
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta que a aplicação usa.
EXPOSE 8084

# Comando para iniciar a aplicação.
ENTRYPOINT ["java", "-jar", "app.jar"]