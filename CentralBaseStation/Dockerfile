FROM ubuntu:latest

LABEL authors="mostafaeltawy"
ENTRYPOINT ["top", "-b"]

FROM maven:3.6.3-jdk-8 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package

FROM openjdk:16
COPY . /app
WORKDIR /app

CMD ["java", "-jar", "/app/target/CentralBaseStation-1.0-SNAPSHOT.jar"]

