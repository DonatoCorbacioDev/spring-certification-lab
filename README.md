# Spring Certification Lab

A personal learning project focused on understanding the core concepts of Spring Framework and Spring Boot through small, practical exercises.

The repository is developed step by step and connects each concept to a real backend project: **Business Contracts Manager 2.0**.

## Current Progress

### Week 1 — Spring Core

Topics covered:

- Inversion of Control
- Spring Beans
- Dependency Injection
- Constructor Injection
- Component Scanning
- Stereotype annotations
- `@Primary`
- `@Qualifier`
- `ApplicationContext`
- Bean inspection
- Singleton scope
- Stateless services

## Project Flow

```text
MemberController
        |
        v
MemberService
        |
        +--> MemberRepository
        |
        +--> NotificationService
                  |
                  +--> EmailNotificationService
                  |
                  +--> SmsNotificationService
```

Spring creates and connects these components through the IoC container.

`MemberService` does not create its dependencies with `new`.  
The required Beans are provided through constructor injection.

## Main Components

### `MemberController`

Exposes a demonstration HTTP endpoint and delegates the request to `MemberService`.

### `MemberService`

Contains the application logic and coordinates:

- member persistence;
- notification delivery.

### `MemberRepository`

Simulates the data-access layer used to save a member.

### `NotificationService`

Defines the notification contract without depending on a specific implementation.

Available implementations:

- `EmailNotificationService`
- `SmsNotificationService`

## Constructor Injection

`MemberService` receives its dependencies through the constructor:

```java
public MemberService(
        NotificationService notificationService,
        MemberRepository memberRepository
) {
    this.notificationService = notificationService;
    this.memberRepository = memberRepository;
}
```

Constructor injection makes dependencies:

- explicit;
- mandatory;
- easier to test;
- compatible with immutable `final` fields.

## Multiple Beans of the Same Type

Both notification services implement the same interface:

```text
NotificationService
├── EmailNotificationService
└── SmsNotificationService
```

`@Primary` identifies the default Bean.

`@Qualifier` selects a specific Bean at an injection point.

In this laboratory, `MemberService` explicitly uses:

```java
@Qualifier("smsNotificationService")
NotificationService notificationService
```

Therefore, the registration flow sends an SMS notification.

## ApplicationContext and Bean Inspection

`ApplicationContext` is the main Spring container.

It creates, configures, stores and connects the application Beans.

The project inspects the registered Beans with:

```java
applicationContext.getBeanDefinitionNames();
```

It also retrieves all implementations of `NotificationService` with:

```java
applicationContext.getBeansOfType(
        NotificationService.class
);
```

This confirms that both notification implementations exist inside the container.

## Singleton Scope

`MemberService` is retrieved twice from the `ApplicationContext`:

```java
MemberService first =
        applicationContext.getBean(MemberService.class);

MemberService second =
        applicationContext.getBean(MemberService.class);
```

The following comparison returns `true`:

```java
first == second
```

This demonstrates the default Spring singleton scope: the same Bean instance is reused inside the same `ApplicationContext`.

## Stateless Services

Singleton services should not store mutable, user-specific data in instance fields.

Unsafe example:

```java
private String currentMemberName;
```

Correct approach:

```java
public void registerMember(String memberName)
```

Request-specific data should be passed through:

- method parameters;
- DTOs;
- the database;
- the session;
- the `SecurityContext`.

Stable dependencies can remain immutable fields:

```java
private final NotificationService notificationService;
private final MemberRepository memberRepository;
```

## Demo Endpoint

```http
GET /api/members/register-demo
```

Expected flow:

```text
MemberController
-> MemberService
-> MemberRepository
-> SmsNotificationService
```

Expected console output:

```text
Iscritto salvato nel repository: Donato
Iscritto registrato: Donato
SMS inviato: Benvenuto in biblioteca, Donato
```

The console messages remain in Italian because they are part of the current exercise output. The repository documentation is written in English for professional presentation.

## Run the Project

Requirements:

- Java 21
- Maven

Run from the terminal:

```bash
mvn spring-boot:run
```

Alternatively, run the main Spring Boot class from the IDE.

## Connection to Business Contracts Manager 2.0

The same Spring Core concepts appear in **Business Contracts Manager 2.0**:

```text
ContractController
-> ContractService
-> ContractRepository
```

Spring manages these components as Beans and connects them through constructor injection.

Possible multiple implementations include:

```text
NotificationService
├── EmailNotificationService
└── WhatsAppNotificationService

ExportService
├── PdfExportService
└── ExcelExportService
```

Services such as `ContractService` and `AuthService` are normally singleton Beans and should remain stateless.

These concepts make the backend more modular, replaceable and easier to test.

## Learning Goal

The purpose of this repository is not only to write working code, but also to understand and explain:

- who creates the application objects;
- how Spring discovers components;
- how dependencies are injected;
- how multiple implementations are resolved;
- how Beans are stored in the `ApplicationContext`;
- why singleton services should remain stateless.

## Roadmap

Planned topics:

- Week 1: Spring Core
- Week 2: Spring Boot configuration
- Week 3: REST APIs and validation
- Week 4: JPA and Hibernate
- Week 5: Spring Security
- Week 6: Testing
- Week 7: Logging, Actuator and AOP
- Week 8: Review, interview questions and certification preparation
