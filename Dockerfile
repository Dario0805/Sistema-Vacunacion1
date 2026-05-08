# Etapa 1: Construcción con Maven
FROM maven:3.8.4-openjdk-17 AS build
COPY . /app
WORKDIR /app
RUN mvn clean package

# Etapa 2: Ejecución
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copiamos el WAR y el webapp-runner desde la etapa de construcción
COPY --from=build /app/target/captcha.war app.war
COPY --from=build /app/target/dependency/webapp-runner.jar webapp-runner.jar

# Exponemos el puerto que usa Render
EXPOSE 8080

# Comando para arrancar la aplicación usando webapp-runner
CMD ["java", "-jar", "webapp-runner.jar", "--port", "8080", "app.war"]
