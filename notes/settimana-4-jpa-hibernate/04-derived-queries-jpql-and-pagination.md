# Query derivate, JPQL e paginazione

## Obiettivo

Eseguire filtro, ordinamento e paginazione nel database usando query derivate o JPQL.

## Concetti fondamentali

Spring Data può derivare una query dal nome del metodo:

| Metodo | Interpretazione |
|---|---|
| `findByCategory` | Filtra sulla proprietà `category` |
| `findByPublisher_Id` | Naviga `publisher.id` |
| `findByAuthorContainingIgnoreCase` | Sottostringa case-insensitive |

JPQL usa **entity e proprietà Java**, mentre SQL usa **tabelle e colonne**.

| Tipo | Contenuto |
|---|---|
| `Sort` | Solo ordinamento |
| `Pageable` | Indice pagina, dimensione e ordinamento |
| `Page<T>` | Contenuto e metadati complessivi |

L'indice della prima pagina è `0`. Una `Page` richiede normalmente anche un conteggio per `totalElements` e `totalPages`.

## Flusso interno

```text
Request parameters → Pageable/Sort
  → BookRepository
  → query derivata oppure JPQL
  → Hibernate → SQL
  → Page<BookEntity>
  → page.map(this::toResponse)
  → Page<BookResponse>
```

## Componenti e annotazioni utilizzati

- `BookRepository`
- `@Query`
- `@Param`
- `Pageable`, `Page`, `Sort`
- `@PageableDefault`
- endpoint di ricerca per categoria, editore, titolo e autore

## Codice essenziale

```java
@Query("""
       select b from BookEntity b
       where lower(b.title) like lower(concat('%', :text, '%'))
       """)
Page<BookEntity> searchByTitle(
        @Param("text") String text, Pageable pageable);
```

## Verifiche eseguite

- Categoria `TECHNOLOGY`, size 2, pagine 0–2.
- Risultati osservati: `totalElements = 5`, `totalPages = 3`.
- Titolo contenente `java`: `Effective Java` e `Java Concurrency in Practice`.
- Autore contenente `martin`: ordine ascendente e discendente invertito.
- `publisherId = 2`: soli libri Addison-Wesley.
- Nei log riconosciuti `WHERE`, `ORDER BY`, `OFFSET`, `FETCH` e `COUNT`.

## Errori e differenze importanti

- I nomi delle query derivate usano proprietà Java, non colonne.
- `Sort` non limita il numero di risultati.
- `Pageable` combina paginazione e ordinamento.
- Filtrare dopo `findAll()` sposta inutilmente dati e lavoro nel backend.
- Una query derivata troppo lunga perde leggibilità; JPQL può rendere esplicita l'intenzione.
- `Page.map` trasforma il contenuto conservando i metadati.

## Limiti attuali

Le ricerche sono semplici e non analizzano ottimizzazione avanzata o query native.

## Collegamento a BCM 2.0

Applicare filtri e paginazione nel database limita memoria, traffico e tempo di risposta, mantenendo l'API scalabile rispetto al numero di record.

## Risposta da colloquio in italiano

Spring Data deriva query leggendo nomi di metodi basati sulle proprietà dell'entity. Per query esplicite uso JPQL, che opera su entity e campi Java e viene tradotta da Hibernate in SQL. `Pageable` contiene pagina, dimensione e sort; `Page` aggiunge contenuto e metadati. Nel service uso `Page.map` per produrre DTO.

## Interview answer in English

Spring Data derives queries from repository method names based on entity properties. JPQL explicitly queries entities and Java properties, and Hibernate translates it into SQL. `Pageable` carries paging and sorting parameters, while `Page` also contains results and total metadata.

## Domande di ripasso

1. Come interpreta Spring Data `findByPublisher_Id`?
2. Qual è la differenza tra JPQL e SQL?
3. Che cosa collega `@Param`?
4. Che cosa contiene `Pageable`?
5. Che cosa aggiunge `Page`?
6. Perché la prima pagina è `0`?
7. A cosa serve la query COUNT?
8. Perché evitare `findAll()` seguito da filtro in memoria?
9. **What does `ContainingIgnoreCase` express?**
10. **How does `Page.map` affect pagination metadata?**
11. **When can JPQL be clearer than a derived method name?**

## Concetto da ricordare

**Filtro, sort e pagina appartengono alla query; il service converte il risultato in DTO.**
