# MVC slice e MockMvc

## Obiettivo

Verificare il contratto HTTP di `BookController` isolando il service e senza avviare l'intera applicazione.

## Concetti

- `@WebMvcTest` carica la slice MVC e mantiene reale il controller.
- `@MockitoBean` sostituisce `BookService` nel contesto di test.
- `MockMvc` verifica mapping, JSON, validation e status senza server reale.
- `GlobalExceptionHandler` viene importato per verificare la risposta 404 reale.
- I filtri Security sono esclusi soltanto in questa slice; la sicurezza viene coperta dai test integrati.

## Flusso

```text
MockMvc -> BookController reale -> BookService mock
                    -> Bean Validation / GlobalExceptionHandler
```

## Test eseguiti

- GET esistente: 200 e campi JSON principali.
- POST valido: 201 e response JSON.
- POST non valido: 400 e nessuna interazione col service.
- GET assente: `BookNotFoundException` tradotta in 404.

## Errori e limiti

La slice non verifica filtri Security, transazioni, query JPA o serializzazione con un server di rete. Il service mock dimostra il contratto del controller, non la logica applicativa.

## Collegamento a BCM 2.0

Una slice MVC protegge rapidamente il contratto REST di BCM 2.0 e separa gli errori di binding dai problemi di dominio o persistenza.

## Risposta da colloquio in italiano

`@WebMvcTest` carica solo l'infrastruttura MVC necessaria. Il controller resta reale, mentre il service è sostituito da un mock; così si verificano routing, JSON, validation e gestione errori con un test rapido.

## Interview answer in English

An MVC slice keeps the controller real and replaces its service dependency. It efficiently verifies request mapping, JSON, validation, status codes, and controller advice without loading persistence.

## Domande di ripasso

1. Che cosa carica `@WebMvcTest`?
2. Perché il controller non viene sostituito da un mock?
3. Che differenza c'è tra `MockMvc` e un server reale?
4. Perché il POST invalido non deve chiamare il service?
5. Quando serve importare il controller advice?
6. Perché i filtri Security sono esclusi solo da questa slice?
7. Che cosa dimostra lo status 201?
8. **What does `@MockitoBean` replace in the test context?**
9. **Why should security rules have separate integration tests?**
10. **Which failures can an MVC slice detect?**
