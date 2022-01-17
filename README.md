# Proccessing-Big-file
First  This project upload  Big file next it change some it's columns last write new file with new columns

1. A data vendor delivery data in flat files. These flat files are in matrix format. The first row contains the column labels, the further rows are data rows.
 In the first column holds a data vendor specific identifier. Column separator is a tab.

Example:

COL0	COL1	COL2	COL3

ID1	VAL11	VAL12	VAL12

ID2	VAL21	VAL22	VAL23

2. There is a configuration file that lists the columns that we want to extract. We want to translate the columns to 'our' names.
 So this file contains two columns: first column with original label, second column with 'our' labels.

Example (skip column COL2):

COL0	OURID

COL1	OURCOL1

COL3	OURCOL3


When This application starts to run first fetchs anyfiles which exists following folder that defind in application.properties:
source.file.folder.path=Proccessing-Big-file-main\\files\\source\\
for processing header of matrix system neads a config file in following path:
header.config.file=Proccessing-Big-file-main\\files\\config\\HeaderConfig.txt

3. There is another configuration file that lists the data vendor specific identifiers, so the rows that we want to extract. Similar to point 2: 
these are translated to the values in column 2

Example (skip ID1):

ID2	OURID2

For processing rows of matrix system neads a config file in following path:
row.config.file=Proccessing-Big-file-main\\files\\config\\RowConfig.txt

4. The task is to build a 'translator' that reads in these three files and produces output in the same structure: 
first row with 'our' column labels, further rows with the data we wanted to extract. The output file records don't have to be in the same order as the input.

Example: expected output based on the examples above:

OURID	OURCOL1	OURCOL3

OURID2	VAL21	VAL23

Finaly when the application finishs its processing,Output file is to be created automatically in following path:
process.file.folder.path=Proccessing-Big-file-main\\files\\process\\


----------------------------------------------------------------------
# Spring Boot processing big file Example
Get started with the Spring Boot application, a processing big file example.



##  How to start

1-Run with maven

$ cd Proccessing-Big-file-main


$ mvn spring-boot:run

2- Run with Docker
 
 2-1 docker build .
 
 2-2 docker run  IMAGE_ID
 

##2. log file 

      \Proccessing-Big-file-main\processing.log
