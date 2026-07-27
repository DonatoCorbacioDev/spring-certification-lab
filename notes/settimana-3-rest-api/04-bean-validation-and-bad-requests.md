# Bean Validation e richieste non valide

## Obiettivo

Validare il payload al confine HTTP e distinguere vincoli strutturali da regole applicative.

## Concetti fondamentali

Il backend deve validare l'input indipendentemente dai controlli del frontend.

| Vincolo | Controllo |
|---|---|
| `@NotBlank` | Stringa non nulla, non vuota e non composta solo da spazi |
| `@NotNull` | Valore non nullo |
| `@Positive` | Numero maggiore di zero |
| `@Valid` | Attiva la validazione cascata dell'argomento |

I vincoli semplici del contratto appartengono al Request DTO. Regole che dipendono dallo stato o dal caso d'uso appartengono al service o al dominio.

## Flusso interno

```text
JSON → @RequestBody → BookRequest
     → @Valid
     ├── valido → Controller → Service → 201
     └── non valido → errore di validazione → 400
```

Se il binding o la validazione falliscono, il metodo applicativo non prosegue.

## Componenti e annotazioni utilizzati

- `BookRequest`
- `@NotBlank`
- `@NotNull`
- `@Positive`
- `@Valid @RequestBody`
- Bean Validation starter

## Codice essenziale

```java
@NotBlank(message = "Il titolo è obbligatorio")
String title,

@NotNull(message = "Il numero di copie è obbligatorio")
@Positive(message = "Il numero di copie deve essere positivo")
Integer copies
```

Il DTO corrente applica vincoli anche a costo, categoria, data ed editore.

## Verifiche eseguite

- Payload valido con titolo, autore e copie positive: **201 Created**.
- Payload con titolo vuoto, autore composto da spazi e copie negative: **400 Bad Request**.
- Verificato che il repository non venisse modificato nel flusso non valido.

## Errori e differenze importanti

- `@NotNull` non vieta stringhe vuote; `@NotBlank` sì.
- `@Positive` non sostituisce `@NotNull`: il valore nullo richiede un vincolo separato.
- Validazione del DTO e regola di disponibilità di un libro sono responsabilità differenti.
- Un controllo frontend migliora l'esperienza utente ma non protegge il backend.
- **Precisione:** lo status 400 è l'esito osservato nel laboratorio; la forma del body dipende dagli handler configurati.

## Limiti attuali

Non sono documentati gruppi di validazione o vincoli personalizzati. Il test esistente non verifica automaticamente i singoli payload.

## Collegamento a BCM 2.0

I Request DTO devono impedire input strutturalmente incompleti; le regole che richiedono dati persistiti restano nel caso d'uso.

## Risposta da colloquio in italiano

Bean Validation permette di dichiarare vincoli sul Request DTO. `@RequestBody` effettua il binding e `@Valid` attiva la validazione. Se un vincolo fallisce, la richiesta viene interrotta prima del service e nel laboratorio restituisce 400 Bad Request. Mantengo nel DTO i vincoli di forma e nel service le regole applicative.

## Interview answer in English

Bean Validation declares input constraints on the request DTO. `@RequestBody` performs binding and `@Valid` triggers validation. When a constraint fails, processing stops before the service; the laboratory observed `400 Bad Request`. Structural constraints belong at the API boundary, while business rules belong in the application or domain layer.

## Domande di ripasso

1. Perché il backend valida anche quando esiste un frontend?
2. Che differenza c'è tra `@NotNull` e `@NotBlank`?
3. Perché `@Positive` è accompagnato da `@NotNull`?
4. Che cosa attiva `@Valid`?
5. Dove appartiene una regola che dipende dalle copie disponibili?
6. Quale status è stato osservato con payload non valido?
7. Il service viene eseguito dopo un errore di validazione?
8. Quali campi aggiuntivi valida oggi `BookRequest`?
9. **What is the role of `@RequestBody` versus `@Valid`?**
10. **Why are client-side checks insufficient?**
11. **Where should business validation be implemented?**

## Concetto da ricordare

**Il DTO protegge la forma dell'input; il service e il dominio proteggono le regole del caso d'uso.**
