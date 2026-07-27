# Starter e dependency management

## Obiettivo

Comprendere come gli starter raggruppano dipendenze coerenti e come Maven distingue dipendenze dirette e transitive.

## Concetti fondamentali

Uno **starter** è una dipendenza descrittiva che porta un insieme coerente di librerie per una funzionalità.

| Dipendenza del laboratorio | Scopo |
|---|---|
| `spring-boot-starter-webmvc` | Spring MVC e applicazioni web |
| `spring-boot-starter-validation` | Bean Validation |
| `spring-boot-starter-actuator` | Endpoint operativi |
| `spring-boot-devtools` | Supporto allo sviluppo |

- Una dipendenza **diretta** è dichiarata nel `pom.xml`.
- Una dipendenza **transitiva** è richiesta da un'altra dipendenza.
- Il parent Spring Boot gestisce versioni compatibili; omettere una versione non significa usare una versione casuale.

## Flusso interno

```text
pom.xml → Maven
        → dependency management
        → dipendenze dirette
        → dipendenze transitive
        → classpath applicativo
        → auto-configurazione condizionale
```

## Componenti e annotazioni utilizzati

- Maven e `pom.xml`
- Spring Boot parent
- starter web MVC, validation e actuator
- Actuator health endpoint

## Codice essenziale

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

## Verifiche eseguite

- Eseguito `mvn dependency:tree` per osservare l'albero.
- Chiamato `GET /actuator/health`.
- Risultato osservato:

```json
{"status":"UP"}
```

## Errori e differenze importanti

- Uno starter non è una singola funzionalità eseguibile: aggrega dipendenze.
- Maven risolve gli artefatti; Spring Boot usa il classpath risultante per configurare l'applicazione.
- Actuator non sostituisce test o osservabilità completa: l'health endpoint indica lo stato esposto dai contributor configurati.
- Le dipendenze di sicurezza e JPA appartengono alle settimane successive, non alla verifica originaria di questo giorno.

## Limiti attuali

La nota verifica solo l'albero Maven e l'endpoint health; non analizza ogni dipendenza transitiva.

## Collegamento a BCM 2.0

Gli starter permettono di dichiarare capacità applicative mantenendo un insieme di versioni compatibili e un `pom.xml` leggibile.

## Risposta da colloquio in italiano

Uno starter Spring Boot raggruppa dipendenze coerenti per una funzionalità. Maven distingue dipendenze dirette e transitive, mentre il dependency management di Spring Boot fornisce versioni compatibili. Il classpath risultante contribuisce alle decisioni di auto-configurazione.

## Interview answer in English

A Spring Boot starter provides a coherent dependency set for a specific capability. Maven resolves direct and transitive dependencies, while Spring Boot dependency management supplies compatible versions. The resulting classpath is then used by conditional auto-configuration.

## Domande di ripasso

1. Che cos'è uno Spring Boot starter?
2. Qual è la differenza tra dipendenza diretta e transitiva?
3. Chi risolve l'albero delle dipendenze?
4. Quale ruolo ha il parent Spring Boot?
5. Che cosa espone Actuator nel test eseguito?
6. Che cosa indica lo stato `UP` osservato?
7. In che modo il classpath influenza Spring Boot?
8. Perché non si specifica sempre la versione di ogni libreria?
9. **What does `mvn dependency:tree` show?**
10. **How do starters differ from auto-configuration?**
11. **Which endpoint was used to verify Actuator?**

## Concetto da ricordare

**Gli starter costruiscono il classpath; il dependency management ne mantiene coerenti le versioni.**
