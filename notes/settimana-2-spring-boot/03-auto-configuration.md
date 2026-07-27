# Auto-configurazione di Spring Boot

## Obiettivo

Comprendere come Spring Boot propone configurazioni in base al contesto applicativo, senza considerare il processo come “magia”.

## Concetti fondamentali

L'auto-configurazione è **condizionale**. Le decisioni dipendono soprattutto da:

1. classi presenti nel **classpath**;
2. bean già definiti nell'`ApplicationContext`;
3. **properties** disponibili nell'`Environment`;
4. tipo di applicazione e altre condizioni dichiarate.

Formula di ripasso:

```text
Classpath + bean + properties + condizioni → configurazione applicabile
```

Molte auto-configurazioni applicano il principio di **back off**: se l'applicazione fornisce un bean compatibile, la configurazione predefinita può non crearne un altro.

## Flusso interno

```text
Starter → classi nel classpath
        → valutazione delle condizioni
        → registrazione dei bean mancanti
        → personalizzazione tramite properties
```

Esempi del laboratorio:

- starter web MVC → infrastruttura Spring MVC e server web;
- starter actuator → infrastruttura Actuator e `/actuator/health`.

## Componenti e annotazioni utilizzati

- `@SpringBootApplication`
- `@EnableAutoConfiguration` come meta-annotazione
- `AutoConfigurationController`
- `ApplicationContext` ed `Environment`

## Codice essenziale

```java
@SpringBootApplication
public class SpringCertificationLabApplication { ... }
```

Non è necessario importare manualmente ogni configurazione MVC di base.

## Verifiche eseguite

- `GET /api/autoconfig/info`: risposta sui criteri classpath, bean e properties.
- `GET /api/autoconfig/web`: conferma didattica del contributo dello starter web MVC.
- Disponibilità di `/actuator/health` con Actuator nel classpath.

## Errori e differenze importanti

- Starter e auto-configurazione non coincidono: lo starter porta dipendenze, l'auto-configurazione valuta condizioni e registra bean.
- L'auto-configurazione non impedisce la personalizzazione.
- Un bean personalizzato non disabilita “tutto Spring Boot”: può far arretrare una specifica configurazione condizionale.
- Le properties influenzano il comportamento senza cambiare il codice Java.

## Limiti attuali

Non sono stati analizzati report delle condizioni o singole classi di auto-configurazione.

## Collegamento a BCM 2.0

Conoscere le condizioni consente di capire quali componenti vengono creati automaticamente e dove introdurre personalizzazioni esplicite.

## Risposta da colloquio in italiano

Spring Boot applica auto-configurazioni condizionali in base a classpath, bean esistenti, properties e tipo di applicazione. Gli starter rendono disponibili le librerie; le auto-configurazioni valutano il contesto e registrano bean appropriati. Se l'applicazione fornisce una configurazione compatibile, Spring Boot spesso applica il back off.

## Interview answer in English

Spring Boot applies conditional auto-configuration based on the classpath, existing beans, configuration properties, and the application type. Starters provide dependencies, while auto-configuration evaluates conditions and registers suitable beans. User-defined beans can cause a specific default configuration to back off.

## Domande di ripasso

1. Da quali informazioni dipende l'auto-configurazione?
2. Che cosa significa configurazione condizionale?
3. Qual è la differenza tra starter e auto-configurazione?
4. Che cosa indica il principio di back off?
5. In che modo le properties influenzano i bean?
6. Quale starter rende disponibile l'infrastruttura MVC?
7. Perché l'auto-configurazione non è “magia”?
8. Che cosa non implica la definizione di un bean personalizzato?
9. **Which inputs drive Spring Boot auto-configuration?**
10. **What does “back off” mean?**
11. **Do starters themselves register every application bean?**

## Concetto da ricordare

**L'auto-configurazione propone bean solo quando le condizioni del contesto lo consentono.**
