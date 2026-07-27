# Configurazione esterna

## Obiettivo

Separare i valori configurabili dal codice Java e comprendere come Spring Boot li rende disponibili all'applicazione.

## Concetti fondamentali

La **externalized configuration** permette allo stesso artefatto di usare valori diversi senza modificare la logica.

| Formato o fonte | Uso |
|---|---|
| `application.properties` | Coppie chiave-valore |
| `application.yml` | Struttura gerarchica basata sull'indentazione |
| Variabili d'ambiente | Configurazione fornita dall'ambiente |
| Argomenti di avvio | Override al momento dell'esecuzione |

`@Value` è adatto a pochi valori isolati; `@ConfigurationProperties` è preferibile per gruppi coerenti e tipizzati.

## Flusso interno

```text
Fonti di configurazione
  → Environment
  → risoluzione delle property
  → injection nei bean
  → comportamento configurato
```

## Componenti e annotazioni utilizzati

- `application.yaml`
- `ConfigurationInfoController`
- `@Value("${app.message}")`
- `@Value("${app.owner}")`
- proprietà `server.port`, `spring.application.name`, `app.*` e logging

## Codice essenziale

```java
public ConfigurationInfoController(
        @Value("${app.message}") String appMessage,
        @Value("${app.owner}") String appOwner) {
    ...
}
```

## Verifiche eseguite

Nella fase originale la porta era stata impostata a `8081` e sono stati verificati:

- `GET /api/config/message`
- `GET /api/config/owner`
- `GET /api/config/info`
- `GET /actuator/health`

La configurazione corrente comune usa la porta `8080`; i profili possono sovrascriverla.

## Errori e differenze importanti

- **Correzione di contesto:** la nota originale presentava `8081` come valore generale; oggi è il valore del profilo `dev`, mentre `application.yaml` usa `8080`.
- YAML e properties esprimono lo stesso tipo di configurazione, ma hanno sintassi diversa.
- L'indentazione YAML è significativa.
- Spostare un segreto in un file versionato non lo rende sicuro.
- `@Value` ripetuto su molti campi può rendere meno coesa una configurazione complessa.

## Limiti attuali

Il progetto usa valori didattici e non definisce una classe `@ConfigurationProperties`.

## Collegamento a BCM 2.0

Porta, datasource, logging e valori specifici dell'ambiente devono essere configurabili senza ricompilare la logica applicativa. I dati sensibili richiedono fonti esterne appropriate.

## Risposta da colloquio in italiano

La configurazione esterna separa i valori ambientali dal codice. Spring Boot raccoglie property da file, variabili d'ambiente e argomenti e le espone tramite l'`Environment`. Per valori isolati posso usare `@Value`; per gruppi coerenti preferisco `@ConfigurationProperties`, che offre binding tipizzato.

## Interview answer in English

Externalized configuration separates environment-specific values from application logic. Spring Boot collects properties from configuration files, environment variables, and command-line arguments. `@Value` suits isolated values, while `@ConfigurationProperties` is better for cohesive, type-safe configuration groups.

## Domande di ripasso

1. Quale problema risolve la configurazione esterna?
2. Che ruolo ha l'`Environment`?
3. Come differiscono YAML e properties?
4. Quando è appropriato `@Value`?
5. Quando preferire `@ConfigurationProperties`?
6. Perché l'indentazione YAML è importante?
7. Qual è la porta comune attuale e quale quella `dev`?
8. Perché un segreto non deve essere salvato in un file versionato?
9. **Which configuration sources can Spring Boot read?**
10. **When is `@ConfigurationProperties` preferable to `@Value`?**
11. **Can externalized configuration change behavior without recompiling Java code?**

## Concetto da ricordare

**La configurazione varia per ambiente; il codice applicativo rimane lo stesso.**
