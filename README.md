# Invoices app
Invoices app is simple yet robust accounting platform, designed to be easily extendable. Multiple databases were implemented. You can choose REST and SOAP web services or simple Front-End to communicate comfortably with the app. Application is licensed under [MIT](https://en.wikipedia.org/wiki/MIT_License).

## Tech stack
<p float="left">
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Spring_Framework_Logo_2018.svg/320px-Spring_Framework_Logo_2018.svg.png" alt="spring" width="200"/>&nbsp;&nbsp;&nbsp;&nbsp;
<img src="https://www.ixpole.com/wp-content/uploads/2018/05/Swagger-logo-300x106.png" alt="swagger" width="200"/>&nbsp;&nbsp;&nbsp;&nbsp;
<img src="https://cdn-images-1.medium.com/max/800/1*AiTBjfsoj3emarTpaeNgKQ.png" alt="junit" width="200"/>&nbsp;&nbsp;&nbsp;&nbsp;
<img src="https://www.jacoco.org/images/jacoco.png" alt="jacoco" width="200"/>
<img src="https://www.ydop.com/wp-content/uploads/2015/06/json-logo-300x143.png" alt="json" width="200"/>&nbsp;&nbsp;&nbsp;&nbsp;
<img src="http://fruzenshtein.com/wp-content/uploads/2014/01/Hibernate-logo.png" alt="hibernate" width="200"/>&nbsp;&nbsp;&nbsp;&nbsp;
<img src="https://d1.awsstatic.com/rdsImages/postgresql_logo.6de4615badd99412268bc6aa8fc958a0f403dd41.png" alt="postgres" width="200"/>&nbsp;&nbsp;&nbsp;&nbsp;
<img src="https://static.javadoc.io/org.mockito/mockito-core/2.27.0/org/mockito/logo.png" alt="mockito" width="200"/>
<img src="http://training.bizleap.com/wp-content/uploads/2018/02/maven-logo.png" alt="maven" width="200"/>   
</p>

## Installation
* JDK 11
* Apache Maven 3.x

## Build and Run
    mvn clean package
    mvn exec:java 
## API
Application is available on localhost:8080. Use `http://localhost:8080/swagger-ui.html#/invoice-controller` to explore REST API using Swagger. You have to log in, to configure login and password, go to `application.properties`:
```
spring.security.user.name=yourLogin
spring.security.user.password=yourPassword
```
To receive an invoice by email, change email address in `application.properties`:
```
spring.properties.receiver=yourEmailAddress@mail.com
```
To test SOAP use Postman or SoapUI. Send requests to `http://localhost:8080/soap/invoices/invoices.wsdl`.
## Database setup
To setup database go to `application.properties`. You can choose from in-memory, in-file and hibernate databases.
```
pl.coderstrust.database=in-memory
pl.coderstrust.database=in-file
pl.coderstrust.database=hibernate
```
Application works correctly without hibernate database.
To use **hibernate**, configure it on your computer using PgAdmin or another tool, and change `application.properties`:
```
spring.datasource.url=yourDatabase
spring.datasource.driverClassName=yourDatabaseDriver
spring.datasource.username=yourDatabaseUsername
spring.datasource.password=yourDatabasePassword
```
## Authors
Project created as part of CodersTrust Java - First Project course by EwaNb, WN and Janusz under protective wings of ziebapawel

<img src="http://i.imgur.com/FdEIPId.jpg" alt="bird" />
