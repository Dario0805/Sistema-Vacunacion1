# Etapa 1: Construcción
FROM maven:3.8.4-openjdk-17 AS build
COPY . /app
WORKDIR /app
# Ejecutamos la compilación
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# CORRECCIÓN AQUÍ: 
# Maven coloca el war en /target/ y el runner en /target/dependency/
COPY --from=build /app/target/captcha.war app.war
COPY --from=build /app/target/dependency/webapp-runner.jar webapp-runner.jar

# Render usa una variable de entorno $PORT, es mejor usarla que fijar el 8080
CMD ["java", "-jar", "webapp-runner.jar", "--port", "8080", "app.war"]
