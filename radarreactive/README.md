# Radar Reactive Backend

## Introduction

This project consists of a Spring Boot Reactive application, used for the Radar frontend to receive the data stored by the Radar Hub.

## Development

### Requirements

- OpenJDK 21

### Build Application

```sh
./gradlew clean build
```

### Run Application

```sh
java -jar build/libs/radarreactive-0.0.1-SNAPSHOT.jar
```

### Format Code

```sh
./gradlew :spotlessApply
```
