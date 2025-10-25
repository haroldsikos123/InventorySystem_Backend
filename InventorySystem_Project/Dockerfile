# Etapa de construcción
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
WORKDIR /app/InventorySystem_Project
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
COPY --from=build /app/InventorySystem_Project/target/InventorySystem_Project-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
