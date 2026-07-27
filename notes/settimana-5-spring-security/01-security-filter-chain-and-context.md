# Security Filter Chain e SecurityContext

## Obiettivo

Comprendere dove Spring Security intercetta una richiesta HTTP, come applica le regole di accesso e dove conserva l'identità autenticata.

## Concetti fondamentali

- Lo starter di Spring Security attiva l'**auto-configurazione**: registra l'infrastruttura di sicurezza e protegge le richieste quando l'applicazione non fornisce regole proprie.
- Una **Servlet Filter** opera prima del controller e può interrompere o proseguire il flusso.
- `DelegatingFilterProxy` collega il container Servlet ai bean gestiti da Spring.
- `FilterChainProxy` seleziona la `SecurityFilterChain` applicabile e ne esegue i filtri.
- `permitAll()` autorizza una richiesta senza richiedere autenticazione.
- `authenticated()` richiede un'identità autenticata, senza imporre uno specifico ruolo.
- I matcher sono valutati in ordine: le regole specifiche devono precedere `anyRequest()`.

| Termine | Significato |
|---|---|
| `Authentication` | Identità corrente, credenziali e authority nel processo di sicurezza |
| `SecurityContext` | Contenitore dell'`Authentication` associata alla richiesta |
| `FACTOR_PASSWORD` | Authority tecnica che indica l'uso del fattore password; **non è un ruolo** |
| Form login | Autenticazione tramite pagina HTML e, nel laboratorio, sessione |
| HTTP Basic | Credenziali inviate nell'header `Authorization` a ogni richiesta |

## Flusso interno

```text
Client
  → DelegatingFilterProxy
  → FilterChainProxy
  → SecurityFilterChain
  → autenticazione
  → SecurityContext
  → autorizzazione
  → Controller
```

Per `/api/security/me`, Spring MVC inietta l'`Authentication` già disponibile nel `SecurityContext`; il controller non autentica l'utente.

## Componenti o configurazione utilizzati

La configurazione del laboratorio dichiara:

```java
.requestMatchers("/api/security/public").permitAll()
.requestMatchers("/api/security/admin").hasRole("ADMIN")
.requestMatchers("/api/security/user").hasAnyRole("USER", "ADMIN")
.anyRequest().authenticated()
.formLogin(Customizer.withDefaults())
.httpBasic(Customizer.withDefaults());
```

Endpoint rilevanti:

- `GET /api/security/public`: accesso pubblico esplicito.
- `GET /api/security/me`: mostra username e authority dell'utente autenticato.

## Verifiche eseguite

- Avvio dell'applicazione con Spring Security presente nel classpath.
- Accesso a `GET /api/security/public` senza credenziali: endpoint raggiungibile.
- Accesso a `GET /api/security/me` dopo autenticazione: risposta con username e authority.
- Osservazione di `ROLE_USER` e `FACTOR_PASSWORD` nell'`Authentication` dell'utente `donato`.

## Errori e differenze importanti

| Confusione | Correzione |
|---|---|
| Il path contiene `public`, quindi è pubblico | È pubblico solo perché la chain usa `permitAll()` |
| `authenticated()` controlla un ruolo | Controlla solo che esista un utente autenticato |
| `FACTOR_PASSWORD` equivale a `ROLE_USER` | È un'indicazione del fattore di autenticazione, non un ruolo |
| Le regole possono essere dichiarate in qualsiasi ordine | Una regola generale anticipata può rendere irraggiungibili quelle specifiche |
| Il controller autentica l'utente | L'autenticazione avviene nei filtri, prima del controller |

## Limiti attuali

- Il laboratorio usa form login e HTTP Basic con impostazioni predefinite.
- Non sono presenti più `SecurityFilterChain`.
- Non sono implementati JWT, OAuth2 o un Resource Server.
- Le credenziali e i ruoli sono didattici.

## Collegamento a BCM 2.0

In BCM 2.0 la filter chain resterà il confine centrale per decidere quali API siano pubbliche, autenticate o riservate. Eventuali token o provider esterni cambieranno il meccanismo di autenticazione, non la separazione tra filtri, `SecurityContext` e controller.

## Risposta da colloquio in italiano

Spring Security intercetta le richieste prima del controller tramite filtri Servlet. `DelegatingFilterProxy` collega il container Servlet a Spring, mentre `FilterChainProxy` esegue la `SecurityFilterChain` selezionata. Dopo l'autenticazione, l'identità è rappresentata da `Authentication` e resa disponibile nel `SecurityContext`. Le regole di autorizzazione sono valutate nell'ordine dichiarato: `permitAll()` consente l'accesso pubblico, mentre `authenticated()` richiede un utente autenticato.

## Interview answer in English

Spring Security intercepts requests before the controller through Servlet filters. `DelegatingFilterProxy` bridges the Servlet container and Spring, while `FilterChainProxy` runs the selected `SecurityFilterChain`. After authentication, the current identity is represented by `Authentication` and stored in the `SecurityContext`. Authorization matchers are evaluated in declaration order.

## Domande di ripasso

1. Qual è la responsabilità di una Servlet Filter nel flusso HTTP?
2. Come collaborano `DelegatingFilterProxy` e `FilterChainProxy`?
3. Perché `anyRequest()` deve seguire i matcher specifici?
4. Che differenza c'è tra `permitAll()` e `authenticated()`?
5. Come arriva l'`Authentication` al parametro di `/api/security/me`?
6. Quale errore produrrebbe una regola generale collocata troppo presto?
7. In che modo form login e HTTP Basic differiscono come trasporto delle credenziali?
8. **What does the `SecurityContext` contain?**
9. **Why is `FACTOR_PASSWORD` not an application role?**
10. **Where does authorization happen relative to the controller?**

## Concetto da ricordare

**La richiesta viene autenticata e autorizzata nella filter chain; il controller riceve un'identità già stabilita nel `SecurityContext`.**
