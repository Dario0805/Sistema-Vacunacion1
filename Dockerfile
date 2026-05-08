# Etapa 1: Construcción con Maven
FROM maven:3.8.4-openjdk-17 AS build
COPY . /app
WORKDIR /app
RUN mvn clean package

# Etapa 2: Ejecución
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copiamos el WAR y el runner
COPY --from=build /app/target/captcha.war app.war
COPY --from=build /app/target/dependency/webapp-runner.jar webapp-runner.jar

# Exponemos el puerto
EXPOSE 8080

# El comando debe ir exactamente así para evitar el 404 y que no salga el menú de ayuda
CMD ["java", "-jar", "webapp-runner.jar", "--port", "8080", "--context-path", "", "app.war"]
