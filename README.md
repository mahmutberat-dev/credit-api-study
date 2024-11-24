# Credit Loan App

This repository provides a Credit Management API to allow both Administrator and End Users to perform basic loan
functionalities such as create, update and retrieve a loan.

## Tech Stack

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA, Lombok, MapStruct
- Liquibase to manage database schema
- H2 as database
- JUnit5
- Postman for API tests

## Starting the service

- Service will be started at port `8082`. To start application from the command line:

```bash
./gradlew bootRun
```

- To package as jar and run the jar

```bash
./gradlew bootJar && java -jar build/libs/study-0.0.1-SNAPSHOT.jar
```

- To running tests

```bash
./gradlew clean test
```

## API Documentation and Tests

- Swagger enabled for the service and the address serves the address: http://localhost:8082/swagger-ui/index.html#/
- Postman collection is available run HTTP
  requests: [API Requests.postman_collection.json](API%20Requests.postman_collection.json). The file can easily be
  imported into Postman. It contains specific request samples to check service endpoints.

## Feature set and Developer notes:

- All application endpoints are secured with Bearer token (JWT). Except for the authentication controller endpoints (
  `/auth/register` and `/auth/login`), all the service APIs are secured and authorized by requested user ID.
- There are two ways to create user:
    - One is by the user. The user can register themselves without specifying a credit limit. However, the admin has to
      update the user's credit limit before creating a loan.
    - The other is by the admin. The admin can create a user with a credit limit.
- The Loan Controller provides functionalities for creating, retrieving, and updating a user's loan. The Loan Service
  contains comprehensive unit tests to meet loan operation requirements such as limit validations, installment creation,
  and more. Feel free to examine the unit tests and invoke the API using the Postman collection. Additionally, Reward
  and Penalty cases are covered.
    - Creating and paying loans involves several validations on both the controller and service layers, such as the
      number of installment validations, and comparisons of the user's current credit limit and used credit limit.
- An initial admin user is set via Liquibase with the username `admin` and password `pass`. It is important to have
  these credentials before calling Admin endpoints.

### Improvement Opportunities

- More unit tests and integration tests can be added.
- Loan amount calculation strategies can be discussed and new calculation strategies can be developed.
- Create Loan and Pay Loan capabilities contains multiple validations. Chain of responsibility pattern would be good fit
  for those validations.