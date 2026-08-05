# Logging professionale

## Obiettivo

Aggiungere eventi diagnostici utili senza cambiare il comportamento o esporre dati sensibili.

## Concetti e flusso

SLF4J separa API e implementazione. I placeholder `{}` evitano concatenazioni premature. `BookService` registra ricerca a DEBUG, assenza a WARN e creazione/prestito a INFO; il loader usa INFO al posto di `System.out`.

```text
richiesta -> BookService -> log metadati minimi -> repository
```

## Test eseguiti

- test unitari mirati del service;
- suite completa per verificare comportamento invariato.

## Errori e limiti

Non vengono registrati request completi, response, credenziali, header o stack trace duplicati. I runner Spring Core mantengono le stampe didattiche intenzionali.

## Collegamento a BCM 2.0

Eventi con identificativi tecnici e livelli coerenti consentono diagnosi dei casi d'uso senza trasformare i log in un archivio di dati applicativi.

## Risposta IT/EN

IT: uso log parametrizzati e livelli coerenti, registrando solo il contesto minimo utile. EN: parameterized logging keeps diagnostics useful while avoiding sensitive payloads and unnecessary formatting.

## Domande

1. Quando usare DEBUG?
2. Quando usare WARN?
3. Perché evitare concatenazioni?
4. Perché non loggare il DTO intero?
5. Quando è appropriato ERROR?
6. Perché evitare stack trace duplicati?
7. Qual è il vantaggio di SLF4J?
8. **What makes a log event actionable?**
