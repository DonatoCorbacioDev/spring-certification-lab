# Debito tecnico e consolidamento finale

## Obiettivo

Chiudere il percorso con una semantica dominio/HTTP precisa, validation coerente e documentazione aggiornata.

## Concetti e flusso

`BookEntity` solleva `BookNotAvailableException` senza conoscere HTTP. Il controller advice traduce l'eccezione nel formato `ErrorResponse` esistente con status 409. Il DTO limita titolo, autore e precisione del costo secondo le colonne JPA.

```text
borrow -> regola entity -> BookNotAvailableException -> advice -> 409
POST   -> Bean Validation -> 400 prima del service
```

## Test eseguiti

- entity: eccezione specifica a copie zero;
- service: propagazione invariata;
- MVC: response 409 e validation dei limiti;
- suite completa e controlli Git finali.

## Errori e limiti

Il formato errori non è stato ridisegnato e non è stato aggiunto un catch-all. Restano esclusi locking, database esterni, migrazioni, token e nuove feature.

## Collegamento a BCM 2.0

Eccezioni di dominio indipendenti da HTTP consentono adattatori diversi e rendono esplicita la traduzione al confine REST.

## Risposta IT/EN

IT: una condizione di business attesa richiede un tipo specifico e uno status coerente, mantenendo l'entity libera da dipendenze web. EN: a specific domain exception preserves domain independence while the HTTP adapter maps it to a stable conflict response.

## Domande

1. Perché non usare `IllegalStateException`?
2. Perché l'entity non deve conoscere HTTP 409?
3. Che cosa significa Conflict?
4. Perché allineare DTO e colonne?
5. Che cosa verifica `@Digits`?
6. Perché non aggiungere un catch-all?
7. Quale contratto riusa il controller advice?
8. **Where should transport-specific mapping live?**
9. **Why validate before persistence?**
10. **Which limitations remain intentional?**
