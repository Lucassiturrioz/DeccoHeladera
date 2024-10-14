# Build stage
#
FROM maven:3.8.6-openjdk-18 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package assembly:single -DskipTests


FROM openjdk:18-jdk-slim
WORKDIR /app
COPY --from=build /app/target/HeladeraSimuladaApp.jar HeladeraSimuladaApp.jar


ENV PORT=8080               
EXPOSE 8080                     

ENTRYPOINT ["java", "-jar", "HeladeraSimuladaApp.jar"]
