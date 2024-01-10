# eTachka Marketplace (Backend)

[eTachka Marketplace](https://etachka-marketplace.space) is a web car marketplace. It is built using React for frontend
and Java
Spring Boot for backend. Designed and developed by a team joint at [TeamChallenge](https://teamchallenge.io).

## Development

This section contains all the useful information for developers.

### Prerequisites

Ensure you have the following installed on your local machine:

- Java 17
- Docker

### Running development environment

To run the development environment, use the `bootDevRun` gradle task. It will set up all necessary services in Docker
and bind it to the application. Once the application is running, you can access it at http://localhost:8080.

### Structure

The project is structured in a standard way for a Spring Boot application, related classes are grouped into packages
based on business logic. This approach is used to keep the codebase clean and easy to navigate.

### Code

The project uses the [Google Java Style](https://google.github.io/styleguide/javaguide.html) conventions. Follow them
when writing code to keep the codebase consistent.

### Deployment

The project is deployed automatically on every push to the `main` branch. To skip deployment, add `[skip ci]` to the
commit message.

### Technologies

The project is built using the following technologies:

- Java 17
- Spring Boot as application framework
- Spring Data JPA for database access
- Spring Security with JWT for authentication
- PostgreSQL as database
- Gradle for build
- Docker for testing and local development
- Lombok for boilerplate code generation
- MapStruct for mapping between DTOs and DTOs
- Swagger for API documentation
- Flyway for database migrations
- Logback for logging
- Testcontainers for running services in Docker for testing and local development
- JUnit 5 for testing
- Mockito for mocking in tests
- Sentry for error and performance monitoring
- GitHub Actions for CI
