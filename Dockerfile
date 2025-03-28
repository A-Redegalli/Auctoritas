# Stage 1: Build with Maven
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run with minimal JVM (distroless)
FROM eclipse-temurin:17-jre-alpine

ENV PRAETOR_PROFILE=docker

# App location
WORKDIR /app

# Copy JAR from previous stage
COPY --from=builder /app/target/*.jar auctoritas-v.1.0.0.jar

# Run the app
ENTRYPOINT ["java", "-Xmx128m", "-Xms64m", "-XX:+UseSerialGC", "-jar", "auctoritas-v.1.0.0.jar"]
