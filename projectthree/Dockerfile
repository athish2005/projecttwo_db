# ============================
# 🏗️ Stage 1: Build the JAR file
# ============================
FROM maven:3.9.9-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies first (for build cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the full source code
COPY src ./src

# Build the application JAR (skip tests for faster builds)
RUN mvn clean package -DskipTests


# ============================
# 🚀 Stage 2: Run the application
# ============================
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Copy the generated JAR from the builder stage
COPY --from=builder /app/target/projectthree-0.0.1-SNAPSHOT.jar app.jar

# Expose the Spring Boot port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
