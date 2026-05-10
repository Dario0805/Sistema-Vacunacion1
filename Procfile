# Etapa 1: Construcción
FROM maven:3.8.4-openjdk-17 AS build
COPY . /app
WORKDIR /app
# Añadimos -DskipTests para saltar pruebas y acelerar el proceso
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución (El resto queda igual)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/captcha.war app.war
COPY --from=build /app/target/dependency/webapp-runner.jar webapp-runner.jar
EXPOSE 8080
CMD ["java", "-jar", "webapp-runner.jar", "--port", "8080", "--context-path", "", "app.war"]
