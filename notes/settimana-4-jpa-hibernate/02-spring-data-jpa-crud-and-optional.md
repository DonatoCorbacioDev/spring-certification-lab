# Spring Data JPA, CRUD e Optional

## Obiettivo

Usare un repository Spring Data JPA per collegare i casi d'uso REST al database senza scrivere un'implementazione CRUD manuale.

## Concetti fondamentali

```java
public interface BookRepository
        extends JpaRepository<BookEntity, Long> { ... }
```

Spring Data crea a runtime un proxy che implementa l'interfaccia e la registra come bean.

| Metodo | Scopo |
|---|---|
| `save` / `saveAll` | Persistenza di una o più entity |
| `findAll` | Lettura di tutte le entity |
| `findById` | Ricerca per id con `Optional` |
| `existsById` | Verifica di esistenza |
| `deleteById` | Eliminazione per id |
| `count` | Conteggio |

`Optional<BookEntity>` rappresenta esplicitamente presenza o assenza. Nel service, `orElseThrow` traduce l'assenza in un errore applicativo.

## Flusso interno

```text
BookController → BookService → BookRepository proxy
               → Spring Data JPA → EntityManager
               → Hibernate → SQL → H2
```

Ritorno:

```text
BookEntity → mapping nel Service → BookResponse → JSON
```

## Componenti e annotazioni utilizzati

- `JpaRepository<BookEntity, Long>`
- `BookRepository`
- `BookService`
- `BookNotFoundException`
- `Optional`
- `BookRequest` e `BookResponse`

## Codice essenziale

```java
BookEntity entity = bookRepository.findById(id)
        .orElseThrow(() -> new BookNotFoundException(id));
```

## Verifiche eseguite

- `GET /api/books`: dati letti da H2.
- `GET /api/books/1`: **200 OK**.
- `GET /api/books/999`: `Optional` vuoto e **404 Not Found**.
- `POST /api/books`: `INSERT`, id generato e **201 Created**.
- Verificato il record nella console H2.

## Errori e differenze importanti

- Non serve creare `BookRepositoryImpl` per le operazioni standard.
- `Optional.get()` senza controllo può generare `NoSuchElementException`.
- Il repository accede ai dati; il service decide come gestire l'assenza.
- `save` può inserire o aggiornare in funzione dello stato dell'entity e delle regole JPA; non significa sempre `INSERT`.
- Restituire direttamente entity dal controller espone il modello persistente.

## Limiti attuali

Il database resta in memoria e la nota copre solo operazioni CRUD e ricerca per id.

## Collegamento a BCM 2.0

I repository riducono il boilerplate, mentre il service mantiene decisioni applicative, mapping ed errori fuori dal livello di persistenza.

## Risposta da colloquio in italiano

Spring Data JPA genera un'implementazione proxy delle interfacce repository. Estendendo `JpaRepository<BookEntity, Long>` ottengo operazioni CRUD senza implementazione manuale. `findById` restituisce `Optional`; nel service uso `orElseThrow` per trasformare l'assenza in un errore applicativo e converto l'entity in DTO.

## Interview answer in English

Spring Data JPA creates a runtime proxy for repository interfaces. Extending `JpaRepository<BookEntity, Long>` provides CRUD operations without a manual implementation. `findById` returns an `Optional`, which the service handles and maps from the persistence entity to the API DTO.

## Domande di ripasso

1. Che cosa rappresentano i due parametri di `JpaRepository`?
2. Chi crea l'implementazione di `BookRepository`?
3. Perché `findById` restituisce `Optional`?
4. Dove viene gestita l'assenza del libro?
5. Perché evitare `Optional.get()` non controllato?
6. `save` produce sempre un INSERT?
7. Perché il controller non restituisce `BookEntity`?
8. Quali status sono stati osservati per id presente e assente?
9. **What is the purpose of a Spring Data repository proxy?**
10. **Which layer maps entities to response DTOs?**
11. **How does `orElseThrow` support error handling?**

## Concetto da ricordare

**Spring Data implementa l'accesso standard; il service conserva decisioni, errori e mapping.**
