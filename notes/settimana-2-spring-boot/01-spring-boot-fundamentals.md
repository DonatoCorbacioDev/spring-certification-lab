# Fondamenti di Spring Boot

## Obiettivo

Distinguere Spring Framework da Spring Boot e comprendere che cosa avviene quando parte l'applicazione.

## Concetti fondamentali

- **Spring Framework** fornisce IoC, Dependency Injection, bean e `ApplicationContext`.
- **Spring Boot** costruisce sopra Spring Framework e riduce la configurazione iniziale tramite starter, auto-configurazione e convenzioni.
- `@SpringBootApplication` combina principalmente:
  - `@SpringBootConfiguration`;
  - `@EnableAutoConfiguration`;
  - `@ComponentScan`.
- `SpringApplication.run(...)` prepara e avvia l'`ApplicationContext`.
- Il server embedded consente di eseguire l'applicazione web senza installare separatamente un application server.

## Flusso interno

```text
main()
  → SpringApplication.run(...)
  → preparazione Environment
  → creazione ApplicationContext
  → auto-configurazione + component scanning
  → avvio server web embedded
  → applicazione pronta
```

## Componenti e annotazioni utilizzati

- `SpringCertificationLabApplication`
- `@SpringBootApplication`
- `SpringApplication`
- `BootInfoController`
- starter web MVC presente nel `pom.xml`

## Codice essenziale

```java
@SpringBootApplication
public class SpringCertificationLabApplication {
    public static void main(String[] args) {
        SpringApplication.run(
                SpringCertificationLabApplication.class, args);
    }
}
```

## Verifiche eseguite

- Applicazione avviata senza installare Tomcat separatamente.
- `GET /api/boot/hello`: endpoint raggiunto.
- `GET /api/boot/info`: endpoint raggiunto.
- Entrambe le verifiche originali sono state eseguite su `localhost:8080`.

## Errori e differenze importanti

- Spring Boot non sostituisce Spring Framework: lo configura e lo rende più rapido da avviare.
- `@SpringBootApplication` non contiene la logica dell'applicazione; abilita configurazione, scanning e auto-configurazione.
- “Embedded” non significa che il server non esista: significa che viene avviato insieme all'applicazione.

## Limiti attuali

Gli endpoint restituiscono semplici stringhe e servono solo a verificare bootstrap e livello web.

## Collegamento a BCM 2.0

La classe principale avvierà lo stesso container responsabile di controller, service, repository e infrastruttura web dell'applicazione.

## Risposta da colloquio in italiano

Spring Boot è costruito sopra Spring Framework. Mantiene IoC e Dependency Injection, ma semplifica bootstrap, dipendenze e configurazione tramite starter e auto-configurazione. `@SpringBootApplication` abilita configurazione Boot, auto-configurazione e component scanning; `SpringApplication.run` crea il contesto e avvia l'applicazione web.

## Interview answer in English

Spring Boot is built on top of Spring Framework. It keeps the core container and dependency injection model while simplifying bootstrap and configuration through starters and auto-configuration. `@SpringBootApplication` combines Boot configuration, auto-configuration, and component scanning.

## Domande di ripasso

1. Qual è la relazione tra Spring Framework e Spring Boot?
2. Quali annotazioni compone `@SpringBootApplication`?
3. Che cosa avvia `SpringApplication.run`?
4. Perché non serve installare separatamente Tomcat?
5. Che cosa significa server embedded?
6. Quali endpoint hanno verificato il bootstrap?
7. Spring Boot sostituisce il container Spring?
8. Quale package viene usato come base per la scansione?
9. **What does `@EnableAutoConfiguration` contribute?**
10. **What is created by `SpringApplication.run`?**
11. **How does Spring Boot relate to Spring Framework?**

## Concetto da ricordare

**Spring Boot accelera l'avvio e la configurazione, ma il cuore resta il container di Spring Framework.**
