FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY api-gateway/target/*.jar api-gateway.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "api-gateway.jar", "--spring.profiles.active=docker"]
