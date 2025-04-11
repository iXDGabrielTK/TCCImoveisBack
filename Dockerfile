# Etapa 1 - Build da aplicação com Maven
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app
COPY . .

# Empacota a aplicação sem executar os testes
RUN mvn clean package -DskipTests

# Etapa 2 - Imagem final com JDK apenas para executar a aplicação
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copia o .jar gerado na etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta que o Spring Boot usa por padrão
EXPOSE 8080

# Executa o jar
ENTRYPOINT ["java", "-jar", "app.jar"]
