# Proccessing-Big-file
First,  This project upload  Big file. next, it changes some  columns of this file. last,this App writes all of rows into new file.. 



1-When This application starts to run first fetches any file which exists following folder that defind in application.properties:
classpath:\\files\\source\\

2-for processing header of matrix system neads a config file in following path:
classpath:\\files\\config\\HeaderConfig.txt

3-For processing rows of matrix system neads a config file in following path:
classpath:\\files\\config\\RowConfig.txt


4-Finaly when the application finishs its processing,Output file is to be created automatically in following path:
classpath:\\files\\process\\


----------------------------------------------------------------------
# Spring Boot processing big file Example
Get started with the Spring Boot application, a processing big file example.



## 1. How to start

$ cd processingBigFile

Docker

git

Optional:

Docker-Compose

Java 1.8

Maven 3.x

Steps

Clone source code from git

$ git clone https://github.com/Arsalanfallah/Proccessing-Big-file.


Maven build will be executes during creation of the docker image.

Note:if you run this command for first time it will take some time in order to download base image from DockerHub

Run Docker Container

$ docker-compose up

##2. log file

  \logs\log.log























