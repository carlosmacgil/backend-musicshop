# Etapa 1: Construcción
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copia los archivos del proyecto
COPY pom.xml .
COPY src ./src

# Compila el proyecto y empaqueta el JAR
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copia el JAR compilado desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Expone el puerto (Render usa por defecto $PORT)
EXPOSE 8080

# Usa la variable de entorno $PORT que Render define automáticamente
ENV PORT 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
