# Etapa 1: Construcción
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY . .
# Forzamos la descarga de dependencias y compilación
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# NOTA: En la etapa 1, los archivos se generan en /app/target/
# El .war se llama captcha.war según tu pom.xml
COPY --from=build /app/target/captcha.war app.war
# El runner se copia a target/dependency/ por el maven-dependency-plugin
COPY --from=build /app/target/dependency/webapp-runner.jar webapp-runner.jar

# Exponemos el puerto
EXPOSE 8080

# Comando simplificado al máximo para evitar errores de parámetros
# Quitamos --context-path porque a veces causa conflictos si va vacío
CMD ["java", "-jar", "webapp-runner.jar", "--port", "8080", "app.war"]
