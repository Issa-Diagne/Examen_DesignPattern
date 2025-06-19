# Étape 1 : builder le projet avec Maven
FROM maven:3.9.1-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : image runtime légère avec JDK 17
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/gestion-dettes-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
