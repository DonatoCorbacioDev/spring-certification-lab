# Autorizzazione basata sui ruoli e status HTTP

## Obiettivo

Distinguere autenticazione e autorizzazione, configurare regole HTTP basate su ruoli e interpretare correttamente gli status 200, 401 e 403.

## Concetti fondamentali

| Concetto | Domanda |
|---|---|
| Autenticazione | Chi è l'utente? |
| Autorizzazione | L'utente autenticato può accedere a questa risorsa? |
| Authority | Permesso rappresentato da una stringa, per esempio `ROLE_ADMIN` |
| Ruolo | Convenzione di authority con prefisso `ROLE_` |

- `hasRole("ADMIN")` controlla `ROLE_ADMIN`.
- `hasAnyRole("USER", "ADMIN")` accetta almeno uno dei due ruoli prefissati.
- `hasAuthority("ROLE_ADMIN")` controlla esattamente la stringa indicata.
- `requestMatchers(...)` associa una o più richieste a una regola.
- `roles("USER")` nel builder di `UserDetails` produce `ROLE_USER`.
- `FACTOR_PASSWORD` descrive un fattore di autenticazione e **non autorizza come ruolo**.

## Flusso interno

```text
Richiesta
  → matcher selezionato in ordine
  → autenticazione necessaria?
  → Authentication con authority
  → confronto con la regola
  → accesso oppure rifiuto
```

Ordine configurato:

1. `/api/security/public` → `permitAll()`
2. `/api/security/admin` → `hasRole("ADMIN")`
3. `/api/security/user` → `hasAnyRole("USER", "ADMIN")`
4. qualsiasi altra richiesta → `authenticated()`

## Componenti o configurazione utilizzati

```java
.requestMatchers("/api/security/public").permitAll()
.requestMatchers("/api/security/admin").hasRole("ADMIN")
.requestMatchers("/api/security/user").hasAnyRole("USER", "ADMIN")
.anyRequest().authenticated()
```

Endpoint:

- `GET /api/security/user`: ammessi `ROLE_USER` e `ROLE_ADMIN`.
- `GET /api/security/admin`: ammesso solo `ROLE_ADMIN`.

## Verifiche eseguite

| Richiesta | Identità | Risultato osservato | Motivo |
|---|---|---:|---|
| Endpoint pubblico | anonima | **200 OK** | `permitAll()` |
| Endpoint protetto | credenziali errate/assenti nel test API | **401 Unauthorized** | autenticazione non stabilita |
| `/api/security/user` | `ROLE_USER` | **200 OK** | ruolo ammesso |
| `/api/security/user` | `ROLE_ADMIN` | **200 OK** | ruolo ammesso |
| `/api/security/admin` | `ROLE_ADMIN` | **200 OK** | ruolo richiesto presente |
| `/api/security/admin` | `ROLE_USER` | **403 Forbidden** | utente autenticato ma ruolo insufficiente |

## Errori e differenze importanti

| Status | Interpretazione nel laboratorio |
|---:|---|
| 200 | Richiesta autenticata/autorizzata, oppure pubblica |
| 401 | Non è stata stabilita un'autenticazione valida |
| 403 | L'identità è nota, ma una regola nega l'accesso |

Possibili errori:

- confondere `hasRole("ADMIN")` con `hasAuthority("ADMIN")`;
- scrivere `hasRole("ROLE_ADMIN")` invece di usare il nome senza prefisso;
- considerare ogni authority un ruolo;
- mettere `anyRequest()` prima dei matcher specifici;
- interpretare ogni 403 come credenziali errate;
- usare il nome dell'endpoint come se definisse una protezione.

## Limiti attuali

- Ogni utente persistito ha un solo `UserRole`.
- Le regole sono solo a livello HTTP; non è presente Method Security.
- Non sono modellati permessi granulari, scope o gerarchie di ruoli.
- Gli utenti e gli endpoint sono didattici.

## Collegamento a BCM 2.0

BCM 2.0 potrà tradurre ruoli o claim dell'identità in authority applicative. Le regole dovranno restare esplicite e ordinate, distinguendo accesso pubblico, autenticazione e privilegi amministrativi senza affidarsi al nome del path.

## Risposta da colloquio in italiano

L'autenticazione stabilisce chi è l'utente; l'autorizzazione decide se può accedere a una risorsa. In Spring Security un ruolo è normalmente un'authority con prefisso `ROLE_`: `hasRole("ADMIN")` verifica quindi `ROLE_ADMIN`, mentre `hasAuthority` confronta la stringa esatta. Un 401 indica che non è stata stabilita un'autenticazione valida; un 403 indica che la richiesta è stata compresa ma l'accesso è negato, per esempio per un ruolo insufficiente.

## Interview answer in English

Authentication establishes who the user is, while authorization decides what that user may access. In Spring Security, a role is conventionally an authority prefixed with `ROLE_`; therefore, `hasRole("ADMIN")` checks for `ROLE_ADMIN`, whereas `hasAuthority` checks the exact string. A 401 means valid authentication was not established, while a 403 means access was denied.

## Domande di ripasso

1. Quale domanda risolve l'autenticazione e quale l'autorizzazione?
2. Che differenza c'è tra ruolo e authority?
3. Quale authority controlla `hasRole("ADMIN")`?
4. Quando useresti `hasAnyRole`?
5. Perché `hasAuthority("ADMIN")` non equivale a `hasRole("ADMIN")`?
6. Come influisce l'ordine dei `requestMatchers`?
7. Perché un utente `ROLE_USER` riceve 403 sull'endpoint admin?
8. Perché `FACTOR_PASSWORD` non soddisfa una regola di ruolo?
9. **What is the semantic difference between HTTP 401 and 403?**
10. **Which authority is created by `roles("USER")`?**
11. **What could happen if a catch-all matcher is declared first?**

## Concetto da ricordare

**`hasRole("X")` verifica `ROLE_X`: 401 riguarda l'autenticazione non stabilita, 403 un accesso negato.**
