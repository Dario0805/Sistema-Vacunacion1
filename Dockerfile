# Etapa 1: Construcción
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Según tu pom.xml, el archivo generado es target/captcha.war
COPY --from=build /app/target/captcha.war app.war
# El plugin de dependencias lo pone en target/dependency/
COPY --from=build /app/target/dependency/webapp-runner.jar webapp-runner.jar

# Exponemos el puerto que usa Render por defecto
EXPOSE 8080

# Comando optimizado:
# Eliminamos cualquier parámetro extra que pueda confundir al runner
ENTRYPOINT ["java", "-jar", "webapp-runner.jar", "--port", "8080", "app.war"]
