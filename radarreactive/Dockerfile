FROM gradle:8.13.0-jdk21-alpine AS builder
COPY ./radarreactive /code/
WORKDIR /code
RUN ./gradlew clean build

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /code/build/libs/radarreactive-0.0.1-SNAPSHOT.jar ./radarreactive.jar
CMD ["java", "-jar", "radarreactive.jar"]