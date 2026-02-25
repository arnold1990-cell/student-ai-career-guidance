# Edutech Backend (Spring Boot 3.2+, Java 21)

Backend service for Student AI Career Guidance with JWT auth, RBAC, Flyway migrations, OpenAPI docs, and production-friendly configuration via environment variables.

## Stack

- Java 21
- Spring Boot 3.2.8 (Maven)
- Spring Web / Validation / Security / Data JPA / Actuator / Mail
- PostgreSQL + Flyway
- JWT authentication (access + refresh)
- Springdoc OpenAPI UI at `/swagger-ui`

## Project Structure

```text
src/main/java/com/edutech
├── config        # security, OpenAPI, app property wiring
├── controller    # REST controllers
├── domain        # JPA entities + enums
├── dto           # request/response DTOs
├── exception     # global exception handling
├── repository    # data access
├── security      # JWT, auth filters, rate-limiting filter
└── service       # business logic interfaces + implementations
```

## Prerequisites

- Java 21
- Maven 3.9+
- PostgreSQL 14+

## Setup

1. Copy env template:

```bash
cp .env.example .env
```

2. Update values in `.env` for your environment.

3. Export variables and run app:

```bash
set -a
source .env
set +a
mvn spring-boot:run
```

> Flyway will automatically create the schema on startup.

## Common Commands

```bash
# Compile
mvn clean compile

# Run tests
mvn test

# Start app
mvn spring-boot:run

# Package jar
mvn clean package

# Run packaged jar
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

## Troubleshooting

### `Could not find or load main class com.edutech.BackendApplication`

This usually happens when launching a Spring Boot fat jar with a classpath-style command such as:

```bash
java -cp target/backend-0.0.1-SNAPSHOT.jar com.edutech.BackendApplication
```

Use one of these instead:

```bash
# Recommended for local development
mvn spring-boot:run

# Recommended for packaged artifact execution
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

## API Documentation

- Swagger UI: `http://localhost:8080/swagger-ui`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Auth Endpoints

- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `POST /api/v1/auth/refresh`
- `GET /api/v1/auth/me`

## RBAC

- `STUDENT`: `/api/v1/students/**`
- `COMPANY`: `/api/v1/companies/**`
- `ADMIN`: `/api/v1/admin/**`

## Notes

- Passwords are hashed with BCrypt.
- CORS origins come from `EDUTECH_CORS_ALLOWED_ORIGINS`.
- Basic rate limiting filter is enabled globally.
