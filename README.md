# Proccessing-Big-file
First  This project upload  Big file next it change some it's columns last write new file with new columns

Hello World sample shows how to deploy SpringBoot RESTful web service application with Docker
Prerequisite

Installed:
Docker
git

Optional:
Docker-Compose
Java 1.8 or 11.1
Maven 3.x
Steps
Clone source code from git

$  git clone https://github.com/dstar55/docker-hello-world-spring-boot .

Build Docker image

$ docker build -t="hello-world-java" .

Maven build will be executes during creation of the docker image.

    Note:if you run this command for first time it will take some time in order to download base image from DockerHub

Run Docker Container

$ docker run -p 8080:8080 -it --rm hello-world-java




1-Run with maven

$ cd Proccessing-Big-file-main
$ mvn clean package
$ mvn spring-boot:run

2- Run with Docker
 
 2-1 docker build .
 
 2-2 docker run  IMAGE_ID
 for example:
 
 

##2. log file 

      \Proccessing-Big-file-main\processing.log
