# Gestione centralizzata degli errori REST

## Obiettivo

Tradurre eccezioni applicative in risposte HTTP coerenti senza distribuire `try/catch` nei controller.

## Concetti fondamentali

| Componente | Responsabilità |
|---|---|
| `BookNotFoundException` | Segnala l'assenza del libro |
| `PublisherNotFoundException` | Segnala l'assenza dell'editore |
| `@RestControllerAdvice` | Applica gestione trasversale ai controller REST |
| `@ExceptionHandler` | Associa un tipo di eccezione a un metodo |
| `ErrorResponse` | Contratto uniforme dell'errore |

Status principali del laboratorio:

| Status | Significato |
|---:|---|
| 200 | Operazione riuscita |
| 201 | Risorsa creata |
| 400 | Richiesta non valida |
| 404 | Risorsa non trovata |
| 500 | Errore server non gestito specificamente |

## Flusso interno

```text
GET /api/books/99
  → BookController
  → BookService.findById
  → Optional vuoto
  → BookNotFoundException
  → GlobalExceptionHandler
  → ErrorResponse + 404
```

## Componenti e annotazioni utilizzati

- `GlobalExceptionHandler`
- `BookNotFoundException`
- `PublisherNotFoundException`
- `ErrorResponse`
- `@RestControllerAdvice`
- `@ExceptionHandler`
- `ResponseEntity`

## Codice essenziale

```java
@ExceptionHandler(BookNotFoundException.class)
public ResponseEntity<ErrorResponse> handleBookNotFound(
        BookNotFoundException exception,
        HttpServletRequest request) {
    ...
}
```

`ErrorResponse` contiene timestamp, status, error, message e path.

## Verifiche eseguite

- `GET /api/books/99`: libro assente.
- Il service ha generato `BookNotFoundException`.
- Risultato osservato: **404 Not Found** con body JSON strutturato.
- Verificato che il controller non richiedesse un `try/catch` locale.

## Errori e differenze importanti

- La validation rifiuta input non valido; l'error handling traduce eccezioni emerse nel flusso.
- Non ogni eccezione deve diventare 404: lo status deve rappresentare il tipo di errore.
- Una custom exception esprime il problema applicativo; l'advice decide la rappresentazione HTTP.
- Il timestamp mostrato nell'esempio originale era temporaneo ed è stato rimosso.
- Un 500 non dovrebbe esporre stack trace o dettagli interni al client.

## Limiti attuali

Il gestore copre le eccezioni applicative presenti; non è un catalogo completo di tutti gli errori Spring MVC.

## Collegamento a BCM 2.0

Un contratto d'errore uniforme permette ai client di gestire status e messaggi senza dipendere dalle eccezioni Java interne.

## Risposta da colloquio in italiano

Creo eccezioni applicative specifiche nel service e le traduco al confine HTTP con `@RestControllerAdvice` e `@ExceptionHandler`. In questo modo i controller restano concentrati sulla delega e il client riceve uno status coerente e un body strutturato. Nel laboratorio un libro assente produce 404 e un `ErrorResponse`.

## Interview answer in English

I represent application failures with specific exceptions and translate them at the HTTP boundary using `@RestControllerAdvice` and `@ExceptionHandler`. This keeps controllers focused on delegation and gives clients consistent status codes and structured error bodies. A missing book produces `404 Not Found` in the laboratory.

## Domande di ripasso

1. Quale problema rappresenta `BookNotFoundException`?
2. Che cosa centralizza `@RestControllerAdvice`?
3. Come seleziona un metodo `@ExceptionHandler`?
4. Quali campi contiene `ErrorResponse`?
5. Perché il controller non usa `try/catch`?
6. Che differenza c'è tra validation ed error handling?
7. Perché non tutte le eccezioni corrispondono a 404?
8. Quale risposta è stata osservata per `/api/books/99`?
9. **What is the role of a custom exception?**
10. **Why should stack traces not be exposed to API clients?**
11. **Which layer translates application errors into HTTP responses?**

## Concetto da ricordare

**L'eccezione descrive il problema; l'advice lo traduce in un contratto HTTP.**
