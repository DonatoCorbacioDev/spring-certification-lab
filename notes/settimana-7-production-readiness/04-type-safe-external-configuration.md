# Configurazione esterna type-safe

## Obiettivo

Raggruppare `app.environment`, `app.message` e `app.owner` in un contratto immutabile e validato.

## Concetti e flusso

`AppProperties` è un record con `@ConfigurationProperties("app")`, `@Validated` e `@NotBlank`. La main class abilita la scansione; i controller ricevono l'oggetto tramite constructor injection.

```text
YAML/profilo -> binder -> AppProperties -> controller
```

## Test eseguiti

Un `ApplicationContextRunner` crea un contesto minimo con proprietà di test e verifica i tre valori associati, senza aggiungere un `@SpringBootTest`.

## Errori e limiti

I valori YAML e le response degli endpoint restano invariati. Le properties non contengono segreti e il test non carica MVC, JPA o Security.

## Collegamento a BCM 2.0

Gruppi type-safe rendono esplicite configurazioni obbligatorie e riducono stringhe `@Value` distribuite nei componenti.

## Risposta IT/EN

IT: il binding type-safe centralizza nome, tipo e validazione della configurazione. EN: immutable configuration properties provide one validated contract and constructor-friendly dependency injection.

## Domande

1. Che cosa indica il prefix `app`?
2. Perché usare un record?
3. Quando viene applicato `@NotBlank`?
4. Qual è il limite di `@Value` distribuito?
5. Come interagiscono i profili col binding?
6. Perché usare una sola strategia di registrazione?
7. Che cosa carica `ApplicationContextRunner`?
8. **Why validate configuration at startup?**
