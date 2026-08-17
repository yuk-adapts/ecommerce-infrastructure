FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY eureka-server/target/*.jar eureka-server.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "eureka-server.jar", "--spring.profiles.active=docker"]
