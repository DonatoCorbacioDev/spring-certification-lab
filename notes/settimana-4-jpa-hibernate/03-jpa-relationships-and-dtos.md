# Relazioni JPA e DTO

## Obiettivo

Modellare la relazione molti-a-uno tra libri ed editori e attraversarla senza esporre direttamente le entity nell'API.

## Concetti fondamentali

Cardinalità:

```text
PUBLISHERS 1 ───── N BOOKS
PUBLISHERS.ID ← BOOKS.PUBLISHER_ID
```

`BookEntity` è l'**owning side** perché contiene la colonna di join che determina la foreign key.

| Elemento | Significato |
|---|---|
| `@ManyToOne` | Molti libri possono riferirsi allo stesso editore |
| `@JoinColumn` | Colonna FK nell'owning side |
| `optional = false` | Associazione obbligatoria nel modello JPA |
| `nullable = false` | Colonna non nulla nello schema generato |
| `FetchType.LAZY` | Richiesta di caricamento differito della relazione |

**Correzione tecnica:** `LAZY` descrive una strategia richiesta al provider; non va interpretata come garanzia assoluta sul momento o sul numero esatto delle query in ogni contesto.

## Flusso interno

```text
POST BookRequest(publisherId)
  → PublisherRepository.findById
  → PublisherEntity
  → new BookEntity(..., publisher)
  → BookRepository.save
  → INSERT con publisher_id
  → BookResponse(publisherId, publisherName)
```

## Componenti e annotazioni utilizzati

- `PublisherEntity` e `PublisherRepository`
- `BookEntity`
- `@ManyToOne(fetch = LAZY, optional = false)`
- `@JoinColumn(name = "publisher_id", nullable = false)`
- `BookRequest.publisherId`
- `BookResponse.publisherId` e `publisherName`
- `PublisherNotFoundException`

## Codice essenziale

```java
@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "publisher_id", nullable = false)
private PublisherEntity publisher;
```

Il mapping a DTO avviene nel service dentro un confine transazionale, così l'accesso alla relazione avviene con un persistence context disponibile.

## Verifiche eseguite

- GET dei libri con `publisherId` e `publisherName`.
- POST con editore esistente: **201 Created**.
- POST con `publisherId = 999`: **404 Not Found**.
- Query H2 su `PUBLISHERS` e su `BOOKS.PUBLISHER_ID`.
- Osservato che più libri condividono lo stesso editore.

## Errori e differenze importanti

- Cardinalità e strategia di fetch sono concetti distinti.
- `mappedBy` appartiene al lato inverso di una relazione bidirezionale; oggi la relazione è unidirezionale.
- Una `List<BookEntity>` in `PublisherEntity` non è necessaria per creare la foreign key.
- `CascadeType.ALL` non va aggiunto automaticamente: il ciclo di vita di editore e libri non è stato definito come unico.
- Esporre entity collegate può rivelare proxy, dettagli interni o grafi ricorsivi.

## Limiti attuali

La relazione è unidirezionale, senza cascade e senza operazioni di eliminazione dell'editore.

## Collegamento a BCM 2.0

Le relazioni persistenti devono essere modellate secondo cardinalità e ciclo di vita reali; l'API può esporre riferimenti e dati selezionati tramite DTO.

## Risposta da colloquio in italiano

`BookEntity` contiene `@ManyToOne` e `@JoinColumn`, quindi è l'owning side e controlla `BOOKS.PUBLISHER_ID`. Il Request DTO porta l'id dell'editore; il service carica la `PublisherEntity`, collega il libro e salva. Il Response DTO espone solo id e nome dell'editore, evitando di pubblicare direttamente il grafo JPA.

## Interview answer in English

`BookEntity` contains `@ManyToOne` and `@JoinColumn`, so it is the owning side controlling the `BOOKS.PUBLISHER_ID` foreign key. The service resolves the publisher id from the request, links the existing entity, and returns selected publisher data through a response DTO.

## Domande di ripasso

1. Qual è la cardinalità tra editori e libri?
2. Perché `BookEntity` è l'owning side?
3. Che differenza c'è tra `optional=false` e `nullable=false`?
4. Che cosa esprime `FetchType.LAZY`?
5. Perché `publisherId` è sufficiente nel Request DTO?
6. Dove viene verificata l'esistenza dell'editore?
7. Quando servirebbe `mappedBy`?
8. Perché non aggiungere automaticamente `CascadeType.ALL`?
9. **Which table contains the foreign key?**
10. **Is lazy loading an absolute guarantee about SQL timing?**
11. **Why should connected entities not be returned directly?**

## Concetto da ricordare

**L'owning side controlla la foreign key; il DTO controlla ciò che attraversa il confine HTTP.**
