# DTO, RequestBody e POST

## Obiettivo

Separare il payload ricevuto dal modello restituito e creare una risorsa tramite POST con status 201.

## Concetti fondamentali

Un **DTO** rappresenta dati trasferiti attraverso un confine. Request e response hanno scopi distinti:

| Modello | Responsabilità |
|---|---|
| `BookRequest` | Dati accettati dal client |
| `BookResponse` | Dati pubblicati dall'API |
| Entity | Modello persistente, introdotto successivamente |

`@RequestBody` chiede a Spring MVC di deserializzare il body nel tipo Java dichiarato. `ResponseEntity` consente di controllare status, header e body.

## Flusso interno

```text
Client → POST /api/books
       → JSON → HttpMessageConverter/Jackson
       → BookRequest → BookController
       → BookResponse
       → 201 Created + JSON
```

## Componenti e annotazioni utilizzati

- `BookRequest`
- `BookResponse`
- `BookController`
- `@PostMapping`
- `@RequestBody`
- `ResponseEntity`
- `HttpStatus.CREATED`

## Codice essenziale

```java
@PostMapping
public ResponseEntity<BookResponse> create(
        @Valid @RequestBody BookRequest request) {
    BookResponse response = bookService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}
```

`@Valid` appartiene alla fase successiva ma nel codice finale completa il confine di input.

## Verifiche eseguite

- Inviato un body JSON a `POST /api/books`.
- Verificato il binding verso `BookRequest`.
- Osservato **201 Created** con `BookResponse` serializzato.
- Nella fase originaria id e disponibilità erano assegnati dal backend, non dal client.

## Errori e differenze importanti

- Request DTO e response DTO non devono per forza avere gli stessi campi.
- Un JSON sintatticamente errato o non convertibile fallisce prima della logica applicativa.
- `ResponseEntity` non è obbligatoria per ogni endpoint, ma è utile quando serve controllare la risposta.
- **Correzione di contesto:** la nota originaria descriveva un DTO con pochi campi e id fisso; il codice attuale include dati bibliografici, copie, categoria, editore e id persistente.

## Limiti attuali

In questa fase il focus era il contratto HTTP; persistenza, relazioni e validazione completa sono trattate nelle note successive.

## Collegamento a BCM 2.0

DTO distinti impediscono al client di controllare campi interni e consentono di evolvere il modello persistente senza esporlo direttamente.

## Risposta da colloquio in italiano

Uso DTO distinti per definire chiaramente input e output dell'API. `@RequestBody` attiva la conversione del JSON nel Request DTO tramite i message converter. Il controller delega il caso d'uso e restituisce `ResponseEntity` con 201 Created e il Response DTO. In questo modo il client non controlla campi interni come identificativi generati.

## Interview answer in English

Separate request and response DTOs define the API contract without exposing internal models. `@RequestBody` triggers JSON deserialization through Spring MVC message converters. The controller delegates the use case and returns a `ResponseEntity` with `201 Created` and the response DTO.

## Domande di ripasso

1. Che cosa rappresenta un DTO?
2. Perché separare Request DTO e Response DTO?
3. Che cosa fa `@RequestBody`?
4. Quale componente converte il JSON?
5. Perché POST restituisce 201 nel laboratorio?
6. Quando è utile `ResponseEntity`?
7. Quali campi non dovrebbe scegliere liberamente il client?
8. Come differisce il DTO attuale dalla prima demo?
9. **Why use separate request and response models?**
10. **What happens before the controller receives a JSON body?**
11. **Which HTTP status represents successful resource creation here?**

## Concetto da ricordare

**Il Request DTO definisce ciò che il client può inviare; il Response DTO ciò che l'API sceglie di esporre.**
