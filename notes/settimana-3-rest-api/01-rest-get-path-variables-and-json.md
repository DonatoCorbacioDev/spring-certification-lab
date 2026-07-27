# REST, GET, path variable e JSON

## Obiettivo

Comprendere come Spring MVC associa una richiesta GET a un controller, estrae dati dal percorso e serializza una risposta in JSON.

## Concetti fondamentali

Un endpoint combina **metodo HTTP**, **percorso** e comportamento. GET è destinato alla lettura e dovrebbe essere **safe**, cioè non modificare lo stato applicativo.

| Annotazione | Funzione |
|---|---|
| `@RestController` | Controller MVC con risposta scritta nel body |
| `@RequestMapping` | Prefisso o regole comuni del controller |
| `@GetMapping` | Mapping di richieste GET |
| `@PathVariable` | Binding di un segmento del path a un parametro |

Jackson converte gli oggetti di risposta in JSON attraverso i message converter configurati da Spring MVC.

## Flusso interno

```text
Client → GET /api/books/{id}
       → DispatcherServlet
       → handler mapping
       → conversione PathVariable in Long
       → BookController
       → BookResponse
       → Jackson
       → JSON
```

## Componenti e annotazioni utilizzati

- `BookController`
- `BookResponse`, record usato come DTO
- endpoint `GET /api/books`
- endpoint `GET /api/books/{id}`
- `@RestController`, `@RequestMapping`, `@GetMapping`, `@PathVariable`

## Codice essenziale

```java
@GetMapping("/{id}")
public BookResponse findById(@PathVariable Long id) {
    return bookService.findById(id);
}
```

## Verifiche eseguite

- Chiamato `GET /api/books`.
- Chiamato `GET /api/books/2`.
- Verificata la conversione del segmento `2` nel parametro `Long id`.
- Osservata la serializzazione del `BookResponse` in JSON.

## Errori e differenze importanti

- `@PathVariable` legge il percorso; `@RequestParam` legge normalmente la query string.
- Un valore non convertibile in `Long` produce un errore di binding prima del service.
- `@RestController` non restituisce automaticamente HTML: il valore di ritorno viene scritto nel body.
- Un GET che modifica dati viola la semantica HTTP anche se tecnicamente può essere mappato.

## Limiti attuali

Questa fase introduce solo lettura, binding e JSON; service, persistenza e gestione centralizzata degli errori vengono approfonditi dopo.

## Collegamento a BCM 2.0

Le stesse regole consentono di esporre collezioni e risorse identificate mantenendo DTO e semantica HTTP espliciti.

## Risposta da colloquio in italiano

Spring MVC associa una richiesta al metodo del controller tramite le annotazioni di mapping. `@PathVariable` estrae un segmento del percorso e lo converte nel tipo del parametro. Un `@RestController` restituisce il valore nel body e Jackson serializza il DTO in JSON. GET dovrebbe essere usato per leggere senza modificare lo stato.

## Interview answer in English

Spring MVC maps an HTTP request to a controller method through mapping annotations. `@PathVariable` extracts a path segment and converts it to the declared parameter type. A `@RestController` writes the return value to the response body, and Jackson serializes the DTO as JSON.

## Domande di ripasso

1. Da quali elementi è definito un endpoint?
2. Quale semantica dovrebbe avere GET?
3. Che cosa combina `@RestController`?
4. Come viene popolato il parametro `id`?
5. Chi converte `BookResponse` in JSON?
6. Che differenza c'è tra `@PathVariable` e `@RequestParam`?
7. Che cosa accade se l'id non è convertibile in `Long`?
8. Quali due endpoint sono stati verificati?
9. **What does `@GetMapping` select?**
10. **Why should a GET request avoid state changes?**
11. **Which component serializes the response DTO?**

## Concetto da ricordare

**Il mapping seleziona il metodo, il binding converte l'input e Jackson produce il JSON.**
