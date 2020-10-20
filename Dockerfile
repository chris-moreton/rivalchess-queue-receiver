FROM openjdk:8

VOLUME /tmp

RUN apt-get -y update
RUN apt-get install -y qt5-default
RUN wget https://github.com/cutechess/cutechess/releases/download/1.2.0/cutechess-cli-1.2.0-linux64.tar.gz
RUN tar -zxvf cutechess-cli-1.2.0-linux64.tar.gz

ARG DEPENDENCY=build/libs

ARG JAR_FILE=build/libs/rivalchess-generator-1.0.0.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

