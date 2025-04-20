FROM gradle:8.13.0-jdk21-alpine AS builder
COPY ./radarhub /code/
WORKDIR /code
RUN ls
RUN ./gradlew clean build

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /code/build/libs/radarhub-0.0.1-SNAPSHOT.jar ./radarhub.jar
CMD ["java", "-jar", "radarhub.jar"]