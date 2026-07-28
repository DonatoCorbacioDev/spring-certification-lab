# Mockito e service unit testing

## Obiettivo

Verificare `BookService` in isolamento, controllando mapping, gestione delle assenze e coordinamento dei repository senza avviare Spring o accedere al database.

## Concetti fondamentali

- Il service sotto test è un oggetto reale costruito tramite constructor injection.
- I repository sono dipendenze esterne al comportamento del service e vengono sostituiti da mock.
- `when(...).thenReturn(...)` definisce il dato restituito da una dipendenza.
- `verify(...)` controlla soltanto interazioni significative.
- `never()` esprime la regola secondo cui un libro non deve essere salvato se l'editore non esiste.
- Entity, DTO, `Optional` ed eccezioni restano oggetti reali.

## Flusso interno

```text
Test JUnit
  → BookService reale
  → repository mock
  → Optional configurato
  → mapping a BookResponse oppure eccezione
  → assertion e verify
```

## Componenti utilizzati

- JUnit Jupiter 6.0.3
- Mockito core e Mockito JUnit Jupiter 5.23.0
- `MockitoExtension`
- `BookService`
- `BookRepository` e `PublisherRepository` mock
- `BookEntity`, `PublisherEntity`, `BookRequest` e `BookResponse` reali

## Codice essenziale

```java
when(bookRepository.findById(id)).thenReturn(Optional.of(book));

BookResponse response = bookService.findById(id);

assertEquals("Effective Java", response.title());
verify(bookRepository).findById(id);
```

```java
when(publisherRepository.findById(publisherId))
        .thenReturn(Optional.empty());

assertThrows(PublisherNotFoundException.class,
        () -> bookService.create(request));
verify(bookRepository, never()).save(any());
```

## Verifiche eseguite

- `mvn -Dtest=BookServiceTest test`
- `mvn test`
- Verifica del mapping dei campi significativi del libro e dell'editore.
- Verifica dei rami `Optional.of` e `Optional.empty`.
- Verifica che il salvataggio non avvenga quando manca l'editore.

## Errori e differenze importanti

| Aspetto | Risultato |
|---|---|
| Dipendenza Mockito extension | Già disponibile transitivamente; `pom.xml` invariato |
| Costruzione del service | Esplicita con i due repository, senza `@InjectMocks` |
| ID delle entity | Restano `null` perché il test non simula la persistenza |
| Validazione del request | Non viene eseguita: appartiene al confine HTTP/Bean Validation |
| Salvataggio riuscito | Il mock restituisce la nuova entity ricevuta e il mapping viene verificato |

## Limiti attuali

- I mock non eseguono JPA né query SQL.
- Il test non apre transazioni e non verifica commit o rollback.
- Le annotazioni `@Transactional` non sono applicate senza proxy Spring.
- Non viene verificato il dirty checking di Hibernate.
- Controller, binding HTTP, Bean Validation e sicurezza restano fuori dal perimetro.

## Collegamento a BCM 2.0

Nei casi d'uso di BCM 2.0 i test isolati del service possono verificare rapidamente decisioni e coordinamento delle porte di persistenza. Test di integrazione separati dovranno poi coprire mapping, query e confini transazionali reali.

## Risposta da colloquio in italiano

Per testare un service in isolamento costruisco l'istanza reale e sostituisco i repository con mock Mockito. Configuro con `when` i risultati necessari, eseguo il metodo pubblico e verifico response, eccezioni e sole interazioni importanti. Questo dimostra la logica di coordinamento del service, ma non il comportamento di JPA, delle transazioni o del database.

## Interview answer in English

To unit-test a service, I instantiate the real service and replace repository dependencies with Mockito mocks. I stub required outcomes, invoke the public method, and assert responses, exceptions, and meaningful interactions. This validates service orchestration, not JPA or transactional behavior.

## Domande di ripasso

1. Perché `BookService` non deve essere un mock?
2. Qual è il vantaggio della costruzione esplicita rispetto a `@InjectMocks`?
3. Quando si usa `when(...).thenReturn(...)`?
4. Perché entity e DTO restano oggetti reali?
5. Quale regola esprime `never()` nello scenario di editore assente?
6. Perché gli ID sono `null` nel test unitario?
7. Che cosa aggiunge lo scenario di creazione riuscita?
8. **What does `MockitoExtension` provide to a Jupiter test?**
9. **Why does a Mockito unit test not verify dirty checking?**
10. **When is interaction verification valuable?**
11. **What is the difference between stubbing and verification?**

## Concetto da ricordare

**Mockito isola le dipendenze: dimostra le decisioni del service, non il comportamento dell'infrastruttura sostituita.**
