# ==========================================
# Stage 1: Build the Application
# ==========================================
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build jar
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================
# Stage 2: Minimal Runtime Environment
# ==========================================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy executable jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Render injects the PORT environment variable
ENV PORT=8080
EXPOSE 8080

# Run Spring Boot application
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]
