# Build stage
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

COPY src src

RUN ./mvnw clean package -DskipTests -DskipITs

# Rename jar to a fixed name so COPY can reference it directly
RUN cp target/*.jar target/app.jar

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

RUN apk add --no-cache postgresql-client

WORKDIR /app

# Now copy using the fixed filename
COPY --from=builder /app/target/app.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]