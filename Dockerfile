# -----------------------------
# Stage 1: Build JAR
# -----------------------------
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /app

# Copy project files from subfolder
COPY projecttwo/pom.xml .
COPY projecttwo/src ./src

RUN mvn clean package -DskipTests

# -----------------------------
# Stage 2: Run JAR
# -----------------------------
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app
COPY --from=build /app/target/projecttwo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]

