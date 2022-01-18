FROM maven:3.6.0-jdk-8 AS build
WORKDIR /build
COPY ./pom.xml ./pom.xml
COPY ./src ./src
ADD files /usr/local/files
RUN mvn clean package

FROM openjdk:8
#WORKDIR /work
COPY --from=build /build/target/ProcessingBigFileJava-0.0.1.jar demo.jar
#ADD target/ProcessingBigFileJava-0.0.1.jar demo5.jar
ENTRYPOINT ["java","-jar","demo.jar"]
