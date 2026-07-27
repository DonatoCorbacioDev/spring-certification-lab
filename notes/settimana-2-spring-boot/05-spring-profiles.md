# Spring Profiles

## Obiettivo

Comprendere come un profilo attivo seleziona configurazioni specifiche mantenendo invariato il codice Java.

## Concetti fondamentali

Il file comune `application.yaml` fornisce valori di base. I file `application-{profile}.yml` aggiungono o sovrascrivono proprietà quando il profilo corrispondente è attivo.

| Profilo | Porta osservata | Environment | Logging |
|---|---:|---|---|
| default | 8080 | `default` | INFO |
| dev | 8081 | `dev` | DEBUG |
| prod | 8082 | `prod` | WARN |

Il profilo non crea automaticamente un ambiente sicuro: determina quali configurazioni o bean profile-specific vengono considerati.

## Flusso interno

```text
application.yaml
  + profilo attivo
  + application-{profile}.yml
  → merge/override delle property
  → Environment
  → bean configurati
```

## Componenti e annotazioni utilizzati

- `application.yaml`
- `application-dev.yml`
- `application-prod.yml`
- `spring.profiles.active`
- `ProfileInfoController` con valori iniettati tramite `@Value`

## Codice essenziale

```text
--spring.profiles.active=dev
--spring.profiles.active=prod
```

## Verifiche eseguite

- Profilo `dev`: porta 8081, `Development profile active`, logging DEBUG.
- Profilo `prod`: porta 8082, `Production profile active - demo configuration`, logging WARN.
- Verificati gli endpoint `/api/profiles/environment`, `/message` e `/info`.
- Confermato che lo stesso codice Java usa valori differenti.

## Errori e differenze importanti

- Un profilo seleziona configurazioni; non modifica il bytecode.
- Le proprietà specifiche possono sovrascrivere quelle comuni, mentre le altre restano ereditate.
- Il file `prod` del laboratorio è dimostrativo, non una configurazione pronta per produzione.
- Password, token e credenziali reali non devono essere versionati.
- I riferimenti a tecnologie future non studiate sono stati rimossi.

## Limiti attuali

Non esiste un file di profilo `test` e non sono presenti bean condizionati con `@Profile`.

## Collegamento a BCM 2.0

I profili possono separare valori locali e di produzione, ma i segreti devono comunque provenire da fonti esterne sicure. Le differenze ambientali non devono infiltrarsi nella logica applicativa.

## Risposta da colloquio in italiano

Gli Spring Profiles permettono di attivare configurazioni diverse per ambiente. Le proprietà comuni sono definite nel file base e quelle del profilo attivo possono sovrascriverle. Il codice Java rimane identico. Un profilo non è però un sistema di sicurezza: i segreti reali devono arrivare da fonti esterne appropriate.

## Interview answer in English

Spring Profiles enable environment-specific configuration. Common properties come from the base configuration, while the active profile can override selected values. The Java code remains unchanged. Profiles do not secure secrets by themselves; sensitive values must come from appropriate external sources.

## Domande di ripasso

1. Come viene nominato un file di configurazione specifico per profilo?
2. Che cosa accade alle proprietà non sovrascritte?
3. Quali porte sono associate a default, dev e prod?
4. Come si attiva un profilo da argomento?
5. Perché il profilo prod è solo dimostrativo?
6. Un profilo rende automaticamente sicuri i segreti?
7. Quali endpoint mostrano il profilo attivo?
8. Che differenza c'è tra profilo e configurazione comune?
9. **How does a profile-specific file interact with the base configuration?**
10. **Does activating a profile change the Java code?**
11. **Why should production secrets remain outside version control?**

## Concetto da ricordare

**Il profilo cambia la configurazione attiva, non la logica dell'applicazione.**
