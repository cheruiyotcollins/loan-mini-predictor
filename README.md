# Getting Started

## About Jia Loan Projections Mini App

This is a spring boot Jia Loan Projections Mini App system 

Spring Boot 2.7.17, spring boot H2 database and java 8 has been used

## Database Setup

In this project H2 db have been used, for production please change application.properties to a database of your choice.
spring boot spring.jpa.hibernate.ddl-auto will generate all the required tables

1. After setting up the project as per the guidelines below and your application has started, create one sample customer using this POST api `http://localhost:9090/jia/customers/register` as each loan 
   will be tied to a particular customer, Please see swagger Api documentation on how to do this.
   
   sample request   
   {

   "customerName":"Collins Cheruiyot",

   "customerMobileNo":"0708521498",

   "email":"kelvincollins86@gmail.com",

   "customerStatus":"GOOD"

   }
2. To create a new loan use this post Api `http://localhost:9090/jia/loans/application` and provide a valid customer id. sample request.

   {

   "principalAmount":10000,

   "loanDuration":300,

   "customerId":1,

   "loanType":"monthly"

   }
 
3. to check Projected Fees use this API `http://localhost:9090/jia/projected/fees/list?id=1` where id is the loan id
4. to check loan installment use this GET API `http://localhost:9090/jia/loans/installments?id=1` where id is the loan id

To get started with this project, you will need to have the following installed on your local machine:

## Dependencies

JDK 8+ Maven 2.7.17+ To build and run the project, follow these steps:

## Setting up the project

Clone the repository with the command `git clone https://github.com/cheruiyotcollins/loan-mini-predictor.git` for https
or `git@github.com:cheruiyotcollins/loan-mini-predictor.git` for ssh
Navigate to the project directory and :

1. Build the project: `mvn clean install -DskipTests` This will build a jar file in target folder
2. If you are running the application as a standalone jar file, be sure to copy `application.properties` into the same
   location as the jar file and run using command `java -jar jarname.jar &`. & is included to run it on background.
3. If you are running the project using an IDE or from command line use: `mvn spring-boot:run` .
4. The application will be available on http://localhost:9090.

## Swagger documentation

Make sure the application is up and access it via link the
endpoint `URI/swagger-ui/index.html i.e http://localhost:9090/swagger-ui/index.html`