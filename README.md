## Spring Boot-3.2.1, Java-17-0-7, JPA PostgreSQL-16, CELAD TEST

Before running the application create database :
> Database name = **celadDB**; username = **postgres**; password= **admin**

Note database initialization:
> The database is initialized on start up in **data.sql** file

Frameworks:
> **mapstruct** is used to map DTO

> **lombok** is used for getter and setter
> > **lombok** is also used for logger

Units tests:
> **jupiter framework and Mockito** are used for units tests
> > **exceptions** are tested

Controller layer:
> No business action on the controller

## Run Spring Boot application
```
mvn spring-boot:run, or right click on UserManagerApplication class and run the app
```

