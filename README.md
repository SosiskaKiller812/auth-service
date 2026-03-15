# Authentication service

REST API is a Java + Spring Boot application for controll users.

## Flow for User Registration and User Login
For JWT – Token based Authentication with Web API, we’re gonna call 3 endpoints:

POST auth/signup for User Registration
POST auth/signin for User Login
POST auth/refresh for User Refresh token

## The project implements the basic architecture of a server application using:

- Spring Boot
- Spring Web
- Spring Data JPA
- Relational database(postgres)
- DTO and mapper
- Layered architecture (Controller → Service → Repository)

> 🚧 The project is ready for use, but some improvements are needed. It is necessary to improve methods marked @Deprecated, add validation, Open API, replace ? in ResponseEntity on classes and other.🚧
