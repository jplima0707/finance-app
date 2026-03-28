FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY common-kafka ./common-kafka
RUN mvn -f common-kafka/pom.xml clean install -DskipTests
COPY services/accounting-service ./services/accounting-service
RUN mvn -f services/accounting-service/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/services/accounting-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
