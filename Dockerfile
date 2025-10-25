# Etapa de construcción
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Establecer directorio de trabajo
WORKDIR /build

# Copiar solo el pom.xml primero para aprovechar el caché de dependencias
COPY pom.xml .

# Descargar dependencias
RUN mvn dependency:go-offline

# Copiar el código fuente
COPY src ./src/

# Empaquetar la aplicación
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:17-jre-alpine

# Crear un usuario no root para ejecutar la aplicación
RUN addgroup -S spring && adduser -S spring -G spring

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el jar desde la etapa de build
COPY --from=build /build/target/*.jar app.jar

# Cambiar al usuario no root
USER spring:spring

# Configurar volumen para archivos temporales
VOLUME /tmp

# Exponer el puerto de la aplicación
EXPOSE 8080

# Configurar punto de entrada con opciones de Java
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
