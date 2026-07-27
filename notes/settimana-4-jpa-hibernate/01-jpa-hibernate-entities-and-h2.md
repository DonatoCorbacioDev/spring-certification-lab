# JPA, Hibernate, Entity e H2

## Obiettivo

Comprendere il mapping object-relational e persistere entity in un database H2 in memoria.

## Concetti fondamentali

| Tecnologia | Ruolo |
|---|---|
| JPA | Specifica Jakarta Persistence per mapping e gestione della persistenza |
| Hibernate | Provider ORM che implementa JPA nel laboratorio |
| H2 | Database relazionale in memoria |
| `EntityManager` | API JPA per interagire con il persistence context |

Con `jdbc:h2:mem:springlab` i dati esistono durante la vita del database in memoria. `ddl-auto: create-drop` crea lo schema all'avvio e lo elimina alla chiusura ordinata.

## Flusso interno

```text
BookEntity → EntityManager/JPA → Hibernate
           → SQL → H2
```

All'interno di una transazione:

```text
persist(entity) → entity managed → flush → INSERT
                                      → commit
```

## Componenti e annotazioni utilizzati

| Annotazione | Effetto |
|---|---|
| `@Entity` | Tipo persistente |
| `@Table(name = "books")` | Tabella associata |
| `@Id` | Identificatore |
| `@GeneratedValue` | Generazione dell'id |
| `@Column` | Vincoli e dettagli di colonna |
| `@Enumerated(EnumType.STRING)` | Persistenza del nome dell'enum |
| `@Transactional` | Confine dell'unità di lavoro |

`BigDecimal` è usato per `replacementCost` perché evita le approssimazioni binarie tipiche di `double` negli importi decimali.

## Codice essenziale

```java
@Entity
@Table(name = "books")
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
```

Il costruttore senza argomenti non privato permette al provider JPA di istanziare l'entity.

## Verifiche eseguite

- Osservati `CREATE TABLE`, `INSERT` e id generati nei log SQL.
- Eseguita una query nella console H2 sulla tabella `BOOKS`.
- Trovati i libri iniziali `Clean Code` ed `Effective Java` nella fase originaria.

## Errori e differenze importanti

- JPA definisce il contratto; Hibernate lo implementa.
- `EnumType.STRING` è più leggibile e meno legato all'ordine dell'enum rispetto a `ORDINAL`.
- `flush` sincronizza il persistence context con il database, ma **non equivale al commit**.
- `create-drop` è una scelta didattica e distrugge lo schema a fine esecuzione.
- **Correzione di contesto:** gli endpoint erano scollegati da H2 nel Giorno 1; nel codice finale usano Spring Data JPA.

## Limiti attuali

H2 è volatile e la gestione dello schema non è adatta alla produzione. Questa fase usa direttamente concetti JPA prima dell'astrazione repository.

## Collegamento a BCM 2.0

Entity, mapping e transazioni costituiscono il confine persistente; DTO e API devono rimanere separati da tali dettagli.

## Risposta da colloquio in italiano

JPA è la specifica di persistenza usata per mappare oggetti Java su dati relazionali; Hibernate è il provider che la implementa e genera SQL. Un'entity ha un identificatore e un costruttore compatibile con JPA. Nel laboratorio H2 conserva i dati in memoria e `create-drop` ricrea lo schema a ogni esecuzione.

## Interview answer in English

JPA is the persistence specification used to map Java objects to relational data, while Hibernate is the provider implementing it and generating SQL. An entity has an identifier and a JPA-compatible no-argument constructor. The laboratory uses an in-memory H2 database with a `create-drop` schema strategy.

## Domande di ripasso

1. Qual è la differenza tra JPA e Hibernate?
2. Che cosa gestisce l'`EntityManager`?
3. Perché un'entity richiede un costruttore senza argomenti?
4. Qual è il vantaggio di `EnumType.STRING`?
5. Perché usare `BigDecimal` per importi?
6. Che cosa fa `ddl-auto: create-drop`?
7. Che differenza c'è tra flush e commit?
8. Quali istruzioni SQL sono state osservate?
9. **Is JPA an implementation or a specification?**
10. **What does `persist` do to a new entity?**
11. **Why is an in-memory database unsuitable for durable storage?**

## Concetto da ricordare

**JPA definisce il modello di persistenza, Hibernate lo esegue e H2 conserva i dati della demo.**
