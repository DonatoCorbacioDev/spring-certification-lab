# Data JPA test e repository

## Obiettivo

Verificare mapping e query di `BookRepository` contro H2 reale con una slice JPA transazionale.

## Concetti

- `@DataJpaTest` carica entity, repository e infrastruttura JPA.
- Ogni test viene eseguito in transazione e ripristinato con rollback.
- I dati vengono creati esplicitamente senza dipendere da `BookDataLoader`.
- `flush()` e `EntityManager.clear()` obbligano le verifiche a rileggere dal database.

## Flusso

```text
Test -> repository reale -> Hibernate -> H2
                 flush/clear -> query reale
```

## Test eseguiti

- categoria con `Pageable`, ordinamento e metadati;
- editore tramite `findByPublisher_Id`;
- JPQL titolo case-insensitive;
- autore case-insensitive con `Sort`;
- relazione molti-a-uno e foreign key persistita.

## Errori e limiti

Boot 4 usa starter di test modulari: è stato aggiunto il solo `spring-boot-starter-data-jpa-test`. La slice non verifica service, HTTP, Security o un database diverso da H2.

## Collegamento a BCM 2.0

I repository di BCM 2.0 possono essere verificati allo stesso livello per proteggere query, paginazione e relazioni senza il costo dell'intera applicazione.

## Risposta da colloquio in italiano

`@DataJpaTest` verifica repository e mapping con un database embedded reale. A differenza di Mockito, esegue query e SQL; il rollback mantiene i test indipendenti e `flush/clear` evita risultati dovuti soltanto al persistence context.

## Interview answer in English

`@DataJpaTest` exercises real repositories, mappings, and SQL against an embedded database. Transaction rollback isolates tests, while flush and clear prove that assertions use database reads.

## Domande di ripasso

1. Perché un repository non viene mockato in una slice JPA?
2. Che cosa garantisce il rollback automatico?
3. Perché salvare prima l'editore?
4. Che cosa dimostrano `totalElements` e `totalPages`?
5. A cosa serve `EntityManager.clear()`?
6. Qual è la differenza tra query derivata e JPQL?
7. Perché non usare il data loader?
8. **What does flushing prove in a repository test?**
9. **How does a JPA slice differ from a full integration test?**
10. **Why verify sorting explicitly?**
