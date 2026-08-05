# JUnit Jupiter e unit testing

## Obiettivo

Verificare in isolamento la regola di dominio con cui `BookEntity` presta una copia disponibile e rifiuta un prestito quando le copie sono esaurite.

## Concetti fondamentali

- Un unit test controlla una piccola unità di comportamento senza avviare Spring.
- JUnit Jupiter fornisce `@Test` e le assertion usate per esprimere il risultato atteso.
- `assertEquals` confronta lo stato ottenuto con quello previsto.
- `assertThrows` verifica che uno specifico input attivi il tipo di eccezione previsto.
- Un test deterministico usa dati espliciti e non dipende da database, rete o ordine di esecuzione.

## Flusso interno

```text
BookEntity reale
  → borrowCopy()
  → verifica disponibilità
  → decremento di una copia oppure BookNotAvailableException
  → assertion JUnit
```

## Componenti utilizzati

- JUnit Jupiter 6.0.3, già transitivo dagli starter test modulari di Spring Boot 4.1.
- `BookEntity`
- `PublisherEntity`
- `BookCategory`
- `assertEquals`
- `assertThrows`

## Codice essenziale

```java
BookEntity book = createBook(2);

book.borrowCopy();

assertEquals(1, book.getCopies());
```

```java
BookEntity book = createBook(0);

assertThrows(BookNotAvailableException.class, book::borrowCopy);
```

## Verifiche eseguite

- `mvn -Dtest=BookEntityTest test`
- `mvn test`
- Verifica che il test non usi contesto Spring, repository, mock o database.

## Errori e differenze importanti

| Aspetto | Risultato |
|---|---|
| Eccezione a copie zero | Il codice corrente prevede `BookNotAvailableException` |
| Costruzione dell'entity | È disponibile un costruttore applicativo pubblico completo |
| Costruttore JPA | Resta protetto e non viene modificato per il test |
| Numero iniziale di copie | Il caso positivo parte da `2` e verifica il valore esatto `1` |

## Limiti attuali

- Il test non verifica mapping JPA, vincoli di colonna o relazione nel database.
- Non verifica transazioni, SQL, dirty checking o status HTTP.
- Non affronta concorrenza o locking sul prestito.

## Collegamento a BCM 2.0

Le regole di dominio che cambiano lo stato dovrebbero restare testabili senza infrastruttura. In BCM 2.0 questo riduce il costo di verifica delle invarianti e rende più chiaro il confine tra dominio e persistenza.

## Risposta da colloquio in italiano

Un unit test JUnit Jupiter crea direttamente l'oggetto reale, esegue un comportamento e osserva il risultato senza avviare il framework. Per `BookEntity.borrowCopy`, il caso disponibile verifica il decremento esatto con `assertEquals`; il caso esaurito verifica con `assertThrows` che sia sollevata `BookNotAvailableException`. I test sono rapidi e deterministici perché non coinvolgono Spring o il database.

## Interview answer in English

A JUnit Jupiter unit test creates the real domain object, invokes one behavior, and checks the observable result without starting Spring. The available-copy case asserts the exact decrement, while the exhausted-copy case asserts the specific `BookNotAvailableException` contract.

## Domande di ripasso

1. Che cosa distingue un unit test da un test di integrazione?
2. Perché `BookEntityTest` non usa `@SpringBootTest`?
3. Che cosa dimostra `assertEquals(1, book.getCopies())`?
4. Perché il test parte da almeno due copie?
5. Che cosa verifica `assertThrows`?
6. Perché non si modifica il costruttore JPA protetto?
7. In che modo una factory privata migliora il setup?
8. **Why should each unit test be deterministic?**
9. **What does `assertThrows` return and verify?**
10. **Why is a real entity preferable to a mock in this domain test?**

## Concetto da ricordare

**Un buon unit test verifica una regola osservabile con oggetti reali e senza infrastruttura non necessaria.**
