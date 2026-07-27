# Layer Controller, Service e Repository

## Obiettivo

Separare contratto HTTP, logica applicativa e accesso ai dati in componenti con responsabilità chiare.

## Concetti fondamentali

| Layer | Responsabilità |
|---|---|
| Controller | Binding HTTP, delega e costruzione della risposta |
| Service | Caso d'uso, regole applicative e mapping |
| Repository | Accesso e memorizzazione dei dati |

Un controller “sottile” non significa privo di codice, ma libero da logica applicativa e dettagli di persistenza.

## Flusso interno

```text
Client → BookController → BookService → BookRepository
       ← JSON/HTTP       ← BookResponse ← dati
```

Spring crea i componenti e risolve:

```text
BookController → BookService → BookRepository
```

tramite constructor injection.

## Componenti e annotazioni utilizzati

- `BookController`: `@RestController`
- `BookService`: `@Service`
- repository in memoria nella fase originaria
- `BookRequest` e `BookResponse`

## Codice essenziale

```java
public BookController(BookService bookService) {
    this.bookService = bookService;
}
```

Il codice finale mantiene questa dipendenza, mentre `BookRepository` è ora un repository Spring Data JPA.

## Verifiche eseguite

Nella fase con repository in memoria:

- `GET /api/books`: restituiti tre libri iniziali.
- `POST /api/books`: creato un libro con id generato e **201 Created**.
- Una GET successiva mostrava anche il nuovo libro.
- `GET /api/books/4`: restituito il libro appena inserito.

## Errori e differenze importanti

- Il controller non dovrebbe accedere direttamente al repository se il caso d'uso richiede logica.
- Il service non deve conoscere dettagli HTTP come la serializzazione JSON.
- Un'`ArrayList` non offre persistenza: i dati scompaiono al riavvio.
- **Correzione di contesto:** la nota originaria parlava del repository in memoria come stato corrente; oggi il codice usa `JpaRepository` e H2. La verifica è conservata come fase didattica storica.
- La constructor injection non sostituisce la separazione dei ruoli: rende solo esplicito il grafo.

## Limiti attuali

La demo originaria non gestiva database, transazioni o concorrenza. Questi limiti vengono affrontati nella Settimana 4.

## Collegamento a BCM 2.0

Separare i layer rende i casi d'uso comprensibili e impedisce che HTTP, business logic e persistenza si mescolino nello stesso componente.

## Risposta da colloquio in italiano

Il controller gestisce il confine HTTP e delega. Il service coordina il caso d'uso e le regole applicative. Il repository astrae l'accesso ai dati. Nel laboratorio le dipendenze sono fornite tramite costruttore. La prima implementazione usava memoria volatile; la fase JPA ha poi sostituito quel dettaglio senza cambiare la responsabilità dei layer.

## Interview answer in English

The controller handles the HTTP boundary, the service coordinates application use cases, and the repository abstracts data access. Constructor injection makes these dependencies explicit. The initial in-memory repository was later replaced by Spring Data JPA without changing the architectural responsibilities.

## Domande di ripasso

1. Qual è la responsabilità del controller?
2. Quale logica appartiene al service?
3. Che cosa astrae il repository?
4. Perché il controller non dovrebbe usare direttamente il repository?
5. Che cosa rende esplicita la constructor injection?
6. Quali verifiche mostrarono la volatilità controllabile della demo?
7. Qual è la differenza tra stato storico e codice attuale?
8. Perché un'`ArrayList` non è persistenza?
9. **What makes a controller “thin”?**
10. **Which layer coordinates an application use case?**
11. **What changed when the in-memory repository was replaced?**

## Concetto da ricordare

**Controller parla HTTP, Service coordina il caso d'uso, Repository accede ai dati.**
