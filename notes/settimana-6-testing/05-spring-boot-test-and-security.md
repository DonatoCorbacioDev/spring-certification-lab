# Spring Boot test e sicurezza

## Obiettivo

Verificare la catena Security reale, i ruoli e CSRF insieme al confine MVC e applicativo.

## Concetti

- `@SpringBootTest` carica l'applicazione completa.
- `@AutoConfigureMockMvc` esercita i filtri senza avviare un server.
- `@WithMockUser` crea identità di test senza utenti H2 o credenziali.
- `csrf()` viene aggiunto solo alle richieste non-safe che devono superare il filtro.

## Flusso

```text
MockMvc -> SecurityFilterChain -> Controller -> Service -> JPA -> H2
```

## Test eseguiti

- pubblico anonimo 200 e protetto anonimo 401;
- USER ammesso sul proprio endpoint e respinto dall'ADMIN;
- ADMIN ammesso sul proprio endpoint;
- PATCH autorizzata senza CSRF 403;
- PATCH con CSRF raggiunge l'applicazione e decrementa una copia creata dal test.

## Errori e limiti

È stato aggiunto `spring-boot-starter-security-test`, il modulo ufficiale minimo per le utility Security. Il test non verifica autenticazione con password, form HTML o session cookie.

## Collegamento a BCM 2.0

La stessa matrice protegge operazioni contrattuali distinguendo autenticazione, ruolo applicativo e protezione CSRF.

## Risposta da colloquio in italiano

Un test integrato Security mantiene attiva la filter chain. `@WithMockUser` evita credenziali reali, mentre `csrf()` distingue un'autorizzazione corretta da una richiesta non-safe che soddisfa anche la protezione CSRF.

## Interview answer in English

A security integration test keeps the real filter chain active. Mock users test authorization without credentials, and the CSRF post-processor is used only when a state-changing request should pass that protection.

## Domande di ripasso

1. Qual è la differenza tra 401 e 403?
2. Perché `@WithMockUser` non testa il database utenti?
3. Quando è necessario `csrf()`?
4. Perché GET non richiede un token CSRF?
5. Che cosa prova la PATCH con database esplicito?
6. Perché non usare l'ID del loader?
7. Quale costo aggiunge `@SpringBootTest`?
8. **What does the security filter chain verify before MVC?**
9. **Why is an authenticated user still forbidden without CSRF?**
10. **When should HTTP Basic authentication itself be tested?**
