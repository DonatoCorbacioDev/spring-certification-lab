# Spring Certification Lab

Spring Certification Lab is an educational Spring Boot backend used to practice core framework concepts through a small library API. The code favors explicit layers, focused tests, and production-readiness fundamentals over feature breadth.

## Requirements and stack

- Java 21
- Spring Boot 4.1.x
- Maven
- Spring MVC and Bean Validation
- Spring Data JPA, Hibernate, and H2
- Spring Security with form login, HTTP Basic, and CSRF
- Spring Boot Actuator
- Spring AOP with AspectJ support
- JUnit Jupiter, Mockito, MockMvc, and Spring Security Test

## Architecture

```text
Security filter chain
        -> Controller
        -> Service
        -> Repository
        -> H2
```

Controllers own the HTTP contract, services coordinate transactional use cases, repositories provide persistence, and entities retain domain rules such as copy availability. DTOs keep the persistence model outside the public API.

## Run the application

```bash
mvn spring-boot:run
```

The default profile listens on port `8080`. Activate a profile with a standard Spring Boot mechanism, for example:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Available configurations are:

- default: port `8080`, INFO application logging;
- `dev`: port `8081`, DEBUG application logging;
- `prod`: port `8082`, WARN application logging.

H2 is in-memory and its schema is recreated at startup. Profile files override only their declared values; shared values remain in `application.yaml`.

## Main endpoints

### Library API

- `GET /api/books`
- `GET /api/books/{id}`
- `POST /api/books`
- `PATCH /api/books/{id}/borrow`
- `GET /api/books/search/by-category?category=TECHNOLOGY`
- `GET /api/books/search/by-publisher?publisherId={id}`
- `GET /api/books/search/by-title?text={text}`
- `GET /api/books/search/by-author?text={text}&direction=asc`

A borrow request for a book with no available copies returns HTTP `409 Conflict`. Invalid create requests return HTTP `400` before reaching the service.

### Learning and configuration endpoints

- `/api/config/**`
- `/api/profiles/**`
- `/api/boot/**`
- `/api/starters/**`
- `/api/autoconfig/**`
- `GET /api/members/register-demo`

### Security demonstration endpoints

- `GET /api/security/public`: anonymous access;
- `GET /api/security/me`: authenticated users;
- `GET /api/security/user`: `USER` or `ADMIN`;
- `GET /api/security/admin`: `ADMIN` only.

## Security model

All routes require authentication unless explicitly allowed. The public security demonstration and Actuator health endpoint are anonymous. Role checks use `ROLE_USER` and `ROLE_ADMIN`; CSRF remains enabled for state-changing browser requests.

The application initializes educational database identities with non-recoverable, ephemeral startup values. No usable credentials are documented or stored in source control. Automated authorization tests use Spring Security test identities and do not rely on database accounts.

## Actuator

Only the following endpoints are exposed over HTTP:

- `/actuator/health`: anonymous access, details shown only when authorized;
- `/actuator/info`: `ADMIN` only;
- `/actuator/metrics` and `/actuator/metrics/{name}`: `ADMIN` only.

Sensitive endpoints such as environment, configuration properties, bean listings, dumps, and runtime logger configuration are not exposed.

## Testing strategy

Run all tests with:

```bash
mvn test
```

The suite is layered:

- pure unit tests for entity domain rules;
- Mockito unit tests for service orchestration;
- MVC slice tests for mapping, JSON, validation, and controller advice;
- JPA slice tests with real H2 queries and relationships;
- full integration tests for Security, CSRF, MVC, application logic, and Actuator;
- a small context runner test for type-safe configuration binding.

## Learning notes

Versioned notes live under `notes/`, grouped by week and topic. Weeks 1–5 cover Spring Core, Boot, REST, JPA, and Security. Week 6 documents the testing layers, while Week 7 covers logging, Actuator, AOP, type-safe configuration, and final consolidation.

## Known limitations and future work

This is a learning repository, not a production library platform. It uses an in-memory database, startup demo data, and a deliberately small domain. It does not include database migrations, durable identity provisioning, distributed observability, concurrency control, or a frontend. Those concerns should be evaluated only when a real deployment requires them.
