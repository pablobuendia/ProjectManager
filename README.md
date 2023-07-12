# Project Management API

REST API for managing users and projects. Made with Spring Boot.

## Local DB setup

1. Install PostgreSQL
2. Create database `projectmanager`
3. Create user `projectmanager` with password `secret`
4. Grant all privileges on database `projectmanager` to user `projectmanager`

## Running the application

1. Clone the repository
2. Run `gradlew clean bootRun` in the root directory
3. The application will be available at `localhost:8080`

## Swagger

Swagger documentation is available at `/swagger-ui/index.html#/` endpoint.

## TODO left to do in a second iteration

- [ ] Add more tests
- [ ] Implement sorting
- [ ] Add Spring Security
- [ ] Replace auto-generated SQL queries with custom ones