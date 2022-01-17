#pull base image
FROM openjdk:8-jdk-alpine

ADD files  /usr/local/files

ARG PACKAGED_JAR=target/ProcessingBigFileJava-0.0.1.jar

ADD ${PACKAGED_JAR} app.jar

ENTRYPOINT ["java","-jar","-Xdebug","-Xrunjdwp:server=y,transport=dt_socket,address=8001,suspend=n","/app.jar"]



