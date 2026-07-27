# CSRF, CORS, sessioni e concetti OAuth2

## Obiettivo

Distinguere sicurezza stateful e stateless, interpretare le verifiche CSRF/CORS del laboratorio e separare correttamente JWT, OAuth2 e OpenID Connect senza presentarli come funzionalità implementate.

## Concetti fondamentali

### Stateful e stateless

| Modello | Caratteristica |
|---|---|
| Stateful | L'identità può essere mantenuta tra richieste tramite `HttpSession` e cookie `JSESSIONID` |
| Stateless | Ogni richiesta porta le informazioni necessarie; il server non dipende da una sessione precedente |
| Form login | Nel laboratorio crea un'`Authentication` conservabile nel `SecurityContext` della sessione |
| HTTP Basic | Invia username e password nell'header a ogni richiesta; Base64 non è cifratura |

Il laboratorio è principalmente **stateful**: usa form login, sessione e `JSESSIONID`. HTTP Basic resta disponibile per i client API.

### CSRF e CORS

| Controllo | Domanda |
|---|---|
| CSRF | Una richiesta state-changing che usa credenziali automatiche del browser è legittima? |
| CORS | Questa origine browser può leggere la risposta? |
| Autenticazione | Chi è l'utente? |
| Autorizzazione | Che cosa può fare? |

Un'origine è la combinazione di **protocollo, host e porta**. `http://localhost:3000` e `http://localhost:8080` sono quindi origini diverse.

### Token e protocolli

- **JWT** è un formato di token basato su claim; può essere firmato.
- **OAuth2** è un protocollo/framework di autorizzazione per ottenere e usare access token.
- **OpenID Connect (OIDC)** aggiunge autenticazione e identità sopra OAuth2.
- L'**Access Token** viene presentato al Resource Server per accedere alle API.
- L'**ID Token** descrive l'identità autenticata nel contesto OIDC.
- L'**Authorization Server** gestisce il flusso ed emette token.
- Il **Resource Server** valida access token e protegge risorse.

## Flusso interno

Flusso stateful osservato:

```text
Browser → form login → Authentication → SecurityContext
        → HttpSession → JSESSIONID → richiesta successiva riconosciuta
```

Flusso stateless concettuale, non implementato:

```text
Client → Authorization: Bearer <access-token>
       → validazione token → Authentication
       → autorizzazione → Controller
```

Preflight CORS:

```text
Browser → OPTIONS con Origin e metodo richiesto
        → risposta con header CORS adeguati?
        → sì: possibile richiesta reale
        → no: il browser blocca il flusso cross-origin
```

## Componenti o configurazione utilizzati

Configurazione effettiva:

- form login e HTTP Basic abilitati;
- CSRF lasciato attivo;
- sessione non resa stateless;
- nessuna configurazione CORS;
- utenti H2 caricati da `DatabaseUserDetailsService`.

Non sono stati aggiunti:

- `csrf.disable()` o eccezioni CSRF per `/api/**`;
- `SessionCreationPolicy.STATELESS`;
- `@CrossOrigin` o origini consentite globali;
- filtri JWT, chiavi hard-coded o Bearer authentication;
- OAuth2 Login, Authorization Server o Resource Server.

## Verifiche eseguite

Questi sono **risultati osservati**, non risultati generici attesi:

| Verifica | Risultato osservato | Evidenza |
|---|---:|---|
| `GET /api/security/public` con `Origin: http://localhost:3000` | **200** | assente `Access-Control-Allow-Origin` |
| `OPTIONS` preflight per il `PATCH`, senza CORS configurato | **401** | assenti `Access-Control-Allow-Origin` e `Access-Control-Allow-Methods` |
| `PATCH /api/books/1/borrow` con HTTP Basic e senza token CSRF | **401** | richiesta non completata |
| Stesso `PATCH` con sessione autenticata e senza token CSRF | **403** | sessione valida, token CSRF assente |
| `GET /api/security/admin` con utente `ROLE_USER` | **403** | ruolo insufficiente |
| `GET /api/security/me` con solo `JSESSIONID` dopo login | **200** | identità `donato`, `ROLE_USER` e `FACTOR_PASSWORD` disponibili |

**Osservazione:** `curl` visualizza la risposta del GET anche senza header CORS perché non applica la Same-Origin Policy del browser.

**Spiegazione concettuale:** una richiesta `PATCH` state-changing autenticata tramite cookie di sessione richiede normalmente la protezione CSRF configurata.

**Interpretazione prudente:** il 401 osservato con HTTP Basic e il 401 della preflight indicano che la security chain ha interrotto quelle richieste; senza ulteriori log non va attribuito con certezza assoluta ogni passaggio interno che ha prodotto lo status.

## Errori e differenze importanti

Due risposte **403** osservate hanno cause differenti:

| Richiesta | Identità | Causa |
|---|---|---|
| GET endpoint admin | `ROLE_USER` | autorizzazione negata per ruolo insufficiente |
| PATCH con sessione | autenticata | protezione CSRF senza token |

Altre distinzioni:

- CORS non autentica, non assegna ruoli e non sostituisce CSRF.
- Un 200 visto da `curl` non prova che JavaScript cross-origin possa leggere la risposta.
- HTTP Basic trasporta credenziali; Bearer authentication trasporta un token.
- JWT non definisce il login né coincide con OAuth2.
- Un Access Token e un ID Token hanno scopi differenti.
- Disabilitare CSRF per far funzionare un comando `curl` non sarebbe una conclusione giustificata.

## Limiti attuali

- Nessun frontend cross-origin è collegato.
- Nessuna configurazione CORS è presente.
- L'applicazione dipende dalla sessione e non è configurata come API stateless.
- Non sono implementati JWT, OAuth2, OIDC, Authorization Server o Resource Server.
- Le verifiche sono manuali; il test `contextLoads` non copre questi scenari.

## Collegamento a BCM 2.0

La scelta futura dipenderà dall'architettura reale. Con frontend e backend su origini diverse servirà una allowlist CORS precisa. Con cookie di sessione dovrà essere considerato CSRF. Se Spring Boot diventerà un Resource Server, dovrà validare access token emessi da un Authorization Server affidabile; il laboratorio non anticipa questa scelta con un JWT personalizzato.

```text
Possibile evoluzione, non implementata:
Next.js → Authorization Server → Access Token
        → Spring Boot Resource Server → authority → API
```

## Risposta da colloquio in italiano

Il laboratorio è stateful perché form login associa l'`Authentication` a una sessione riconosciuta tramite `JSESSIONID`. CSRF protegge le richieste che modificano lo stato quando il browser invia automaticamente credenziali come i cookie; CORS stabilisce invece quali origini browser possono leggere una risposta. JWT è un formato di token, OAuth2 è un protocollo di autorizzazione e OpenID Connect aggiunge identità. Un Authorization Server emette token, mentre un Resource Server valida gli access token e protegge le API. Nessun JWT personalizzato è stato implementato.

## Interview answer in English

The laboratory is stateful because form login can associate the `Authentication` with an HTTP session identified by `JSESSIONID`. CSRF protects state-changing requests that may rely on browser-sent credentials, while CORS controls which browser origins may read a response. JWT is a token format, OAuth2 is an authorization protocol, and OpenID Connect adds authentication and identity. No custom JWT system was implemented.

## Domande di ripasso

1. Che differenza c'è tra autenticazione stateful e stateless?
2. Quale ruolo hanno `HttpSession` e `JSESSIONID` nel laboratorio?
3. Perché CSRF è rilevante per una richiesta PATCH con sessione?
4. Come distingui il 403 da ruolo dal 403 da CSRF osservati?
5. Da quali elementi è composta un'origine?
6. Perché il GET con `curl` non dimostra che CORS sia configurato?
7. A cosa serve una richiesta preflight `OPTIONS`?
8. Qual è la differenza tra JWT e OAuth2?
9. Qual è la differenza tra Access Token e ID Token?
10. **Why did the observed preflight fail to provide usable CORS headers?**
11. **What is the difference between an Authorization Server and a Resource Server?**
12. **Why must the observed HTTP Basic PATCH result not be replaced with a generic expected status?**

## Concetto da ricordare

**CSRF protegge richieste state-changing, CORS governa la lettura cross-origin, JWT è un formato e OAuth2 è un protocollo; nel laboratorio i token non sono implementati.**
