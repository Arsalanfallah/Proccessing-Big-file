# Proccessing-Big-file
First  This project upload  Big file next it change some it's columns last write new file with new columns

Hello World sample shows how to deploy SpringBoot RESTful web service application with Docker
Prerequisite

Installed:

Docker

git

Optional:

Docker-Compose

Java 1.8 

Maven 3.x

Steps

Clone source code from git


$  git clone https://github.com/Arsalanfallah/Proccessing-Big-file.

Build Docker image

$ docker build -t="Proccessing-Big-file" .

Maven build will be executes during creation of the docker image.

    Note:if you run this command for first time it will take some time in order to download base image from DockerHub

Run Docker Container

$ docker run  -it --rm Proccessing-Big-file


##2. log file 

      \Proccessing-Big-file-main\processing.log
