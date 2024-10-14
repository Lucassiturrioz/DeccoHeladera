# syntax = docker/dockerfile:1.2 
#
# Etapa de construcción
#
FROM maven:3.8.6-openjdk-18 AS build
WORKDIR /app
COPY pom.xml .               # Copiar el archivo de configuración de Maven
COPY src ./src               # Copiar el código fuente de la aplicación
RUN mvn clean package -DskipTests # Compilar y empaquetar la aplicación, omitiendo las pruebas

#
# Etapa de empaquetado
#
FROM openjdk:18-jdk-slim
WORKDIR /app
COPY --from=build /app/target/HeladeraSimuladaApp.jar HeladeraSimuladaApp.jar # Copiar el archivo JAR desde la etapa de construcción
# Establecer el puerto por defecto
ENV PORT=8080
EXPOSE 8080                 # Exponer el puerto 8080
ENTRYPOINT ["java", "-jar", "HeladeraSimuladaApp.jar"] # Comando de inicio para ejecutar la aplicación
