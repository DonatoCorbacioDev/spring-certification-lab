# Spring AOP e cross-cutting concerns

## Obiettivo

Misurare la durata dei metodi di `BookService` senza mescolare timing e logica applicativa.

## Concetti e flusso

Un advice `@Around` intercetta il solo `BookService`, misura con `System.nanoTime()` e registra firma e durata a DEBUG.

```text
proxy AOP -> start -> proceed una volta -> risultato/eccezione invariati
                                      -> finally: durata
```

## Test eseguiti

- suite completa;
- test integrato con profilo dev per osservare il log DEBUG.

## Errori e limiti

Il timing è diagnostico, non una metrica aggregata. L'aspect non registra argomenti o response e non contiene transazioni, autorizzazione o logica di business.

## Collegamento a BCM 2.0

Un concern trasversale circoscritto permette di osservare casi d'uso senza duplicare codice nei service.

## Risposta IT/EN

IT: `try/finally` garantisce il timing anche in caso di eccezione, che viene propagata invariata. EN: an around advice must call `proceed` exactly once and preserve both return values and exceptions.

## Domande

1. Che cosa intercetta il pointcut?
2. Perché usare `System.nanoTime()`?
3. Perché il log è nel `finally`?
4. Quante volte va chiamato `proceed()`?
5. Perché non loggare gli argomenti?
6. Che differenza c'è tra proxy e oggetto target?
7. Perché il timing è a DEBUG?
8. **What must an around advice preserve?**
