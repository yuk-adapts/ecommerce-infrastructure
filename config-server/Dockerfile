FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY config-server/target/*.jar config-server.jar
COPY config-repo /app/config-repo

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "config-server.jar"]
