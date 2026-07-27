# Transazioni, persistence context e rollback

## Obiettivo

Comprendere come una modifica a un'entity managed viene sincronizzata e come una transazione garantisce commit o rollback del caso d'uso.

## Concetti fondamentali

| Concetto | Significato |
|---|---|
| `@Transactional` | Definisce il confine transazionale |
| Persistence context | Insieme delle entity gestite dall'`EntityManager` |
| Managed entity | Entity collegata al persistence context |
| Dirty checking | Rilevamento delle modifiche a entity managed |
| Flush | Sincronizzazione delle modifiche verso il database |
| Commit | Conferma definitiva della transazione |
| Rollback | Annullamento delle modifiche della transazione |

Per default Spring effettua rollback su `RuntimeException` ed `Error`; le checked exception richiedono normalmente una configurazione esplicita.

## Flusso interno

```text
PATCH /api/books/{id}/borrow
  → BookController
  → BookService @Transactional
  → findById → SELECT → BookEntity managed
  → book.borrowCopy()
  → dirty checking
  → flush → UPDATE
  → commit
```

Se una `RuntimeException` attraversa il confine transazionale:

```text
modifica in memoria → eccezione → rollback → dato DB invariato
```

## Componenti e annotazioni utilizzati

- `BookService.borrowCopy`
- `@Transactional`
- `@Transactional(readOnly = true)` sui metodi di lettura
- `BookEntity.borrowCopy`
- `BookRepository.findById`
- persistence context di Hibernate

## Codice essenziale

```java
@Transactional
public BookResponse borrowCopy(Long id) {
    BookEntity book = bookRepository.findById(id)
            .orElseThrow(() -> new BookNotFoundException(id));
    book.borrowCopy();
    return toResponse(book);
}
```

Non serve `save(book)` perché l'entity è stata caricata e modificata nello stesso persistence context.

## Verifiche eseguite

- PATCH normale: copie diminuite, `SELECT` e `UPDATE` osservati, valore aggiornato in H2.
- Prova temporanea: modifica seguita da `RuntimeException`.
- Valore iniziale `copies = 1`, modifica in memoria a `0`, dopo rollback valore DB ancora `1`.
- Endpoint e metodo dimostrativi del rollback rimossi dopo la verifica.

## Errori e differenze importanti

- `flush` esegue la sincronizzazione, ma **non garantisce il commit**: un rollback successivo può ancora annullare.
- Dirty checking opera su entity managed; una entity detached non riceve lo stesso trattamento automatico.
- `save()` non è necessario nel caso osservato, ma resta utile per nuove entity e altri scenari di stato.
- `readOnly = true` esprime l'intento e consente ottimizzazioni; non va trattato come barriera universale che rende ogni scrittura impossibile.
- Il service è un confine transazionale adatto perché coordina l'intero caso d'uso.
- La chiamata deve attraversare il proxy Spring perché la semantica `@Transactional` venga applicata.

## Limiti attuali

La prova di rollback era manuale e temporanea. Non vengono affrontati propagation, isolation, locking o transazioni distribuite.

## Collegamento a BCM 2.0

Ogni caso d'uso che coordina più modifiche deve avere un confine transazionale coerente, affinché tutte riescano o vengano annullate insieme.

## Risposta da colloquio in italiano

`@Transactional` delimita un'unità di lavoro. Un'entity caricata nella transazione è normalmente managed; Hibernate ne rileva le modifiche tramite dirty checking e genera l'UPDATE al flush. Il flush sincronizza ma non equivale al commit. Se una `RuntimeException` causa rollback, le modifiche non vengono confermate. Nel caso osservato non serve chiamare `save` dopo aver modificato l'entity managed.

## Interview answer in English

`@Transactional` defines a unit-of-work boundary. An entity loaded inside the transaction is normally managed, so Hibernate detects changes through dirty checking and issues an update during flush. Flush synchronizes state but does not equal commit. A runtime exception normally triggers rollback.

## Domande di ripasso

1. Che cosa delimita `@Transactional`?
2. Che cos'è il persistence context?
3. Quando un'entity è managed?
4. Come funziona il dirty checking?
5. Perché `save(book)` non è necessario nel caso osservato?
6. Qual è la differenza tra flush e commit?
7. Quali eccezioni causano rollback per default?
8. Che cosa ha dimostrato la prova con `copies = 1`?
9. Che cosa comunica `readOnly = true`?
10. **Why must transactional calls pass through a Spring proxy?**
11. **Can a transaction roll back after a flush?**
12. **What happens when a managed entity is modified?**

## Concetto da ricordare

**Dirty checking prepara l'UPDATE, il flush sincronizza, il commit conferma e il rollback annulla.**
