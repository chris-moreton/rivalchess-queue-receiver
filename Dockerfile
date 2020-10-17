FROM openjdk:8-jdk-alpine

VOLUME /tmp

COPY . /usr/games/cutechess
COPY . /usr/games/cutechess-cli

ARG DEPENDENCY=build/libs

ARG JAR_FILE=build/libs/rivalchess-generator-1.0.0.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

