# UserDetails e codifica delle password

## Obiettivo

Comprendere come Spring Security carica un utente, rappresenta le sue informazioni e confronta una password presentata con quella codificata.

## Concetti fondamentali

- `UserDetails` è il modello utente richiesto da Spring Security: espone username, password codificata, authority e stato dell'account.
- `UserDetailsService` carica l'utente per username; **non verifica direttamente la password**.
- `InMemoryUserDetailsManager` è un'implementazione di `UserDetailsService` utile per utenti in memoria.
- `PasswordEncoder` codifica e confronta password:
  - `encode(rawPassword)` crea una rappresentazione non reversibile;
  - `matches(rawPassword, encodedPassword)` esegue il confronto.
- `DelegatingPasswordEncoder` riconosce l'algoritmo dal prefisso, per esempio `{bcrypt}`.
- `DaoAuthenticationProvider` coordina `UserDetailsService` e `PasswordEncoder`.

```text
username + password grezza
  → DaoAuthenticationProvider
  → UserDetailsService.loadUserByUsername(username)
  → UserDetails con password codificata
  → PasswordEncoder.matches(raw, encoded)
  → Authentication oppure fallimento
```

## Flusso interno

1. Il client invia username e password.
2. Il provider chiede al `UserDetailsService` l'utente corrispondente.
3. Il servizio restituisce `UserDetails`, inclusa la password già codificata.
4. Il provider usa `PasswordEncoder.matches`.
5. Se credenziali e stato dell'account sono validi, viene creata un'`Authentication`.

La password grezza serve solo al confronto e non deve essere persistita.

## Componenti o configurazione utilizzati

Il laboratorio espone un encoder delegante:

```java
@Bean
PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
}
```

Nella fase iniziale della settimana gli utenti erano forniti tramite `InMemoryUserDetailsManager`; il Giorno 4 lo sostituisce con `DatabaseUserDetailsService`. Il contratto consumato dal provider rimane `UserDetailsService`.

`roles("USER")` crea l'authority `ROLE_USER`; non occorre passare manualmente il prefisso a `roles(...)`.

## Verifiche eseguite

- Autenticazione con credenziali valide: accesso a una risorsa protetta.
- Credenziali errate tramite HTTP Basic: **401 Unauthorized**.
- Lettura delle authority dell'utente autenticato: presenza di `ROLE_USER`.
- Osservazione del prefisso dell'algoritmo nella password codificata.

## Errori e differenze importanti

| Concetto | Significato |
|---|---|
| Encoding della password | Trasformazione one-way verificata con `matches`; non consente di recuperare la password |
| Cifratura | Trasformazione reversibile con una chiave |
| Hash veloce generico | Non è progettato specificamente per rallentare attacchi alle password |
| `UserDetailsService` | Recupera i dati dell'utente |
| `PasswordEncoder` | Codifica e confronta le password |
| `DaoAuthenticationProvider` | Coordina caricamento e verifica |

Errori tipici:

- salvare password grezze;
- confrontare stringhe codificate con `equals`;
- chiamare `encode` sulla password presentata e confrontare i risultati;
- usare `roles("ROLE_USER")`, ottenendo un prefisso duplicato o un errore;
- attribuire al `UserDetailsService` la verifica diretta della password.

## Limiti attuali

- Le credenziali sono esclusivamente didattiche.
- Non sono trattate policy di rotazione, recupero o compromissione delle password.
- L'implementazione in memoria appartiene alla fase precedente; lo stato finale usa H2.
- Non sono presenti fattori aggiuntivi o provider esterni.

## Collegamento a BCM 2.0

BCM 2.0 dovrà mantenere la separazione tra caricamento dell'identità e verifica delle credenziali. Se userà un identity provider esterno, la password potrebbe non essere gestita dall'applicazione; il principio resta quello di non memorizzare mai password grezze.

## Risposta da colloquio in italiano

`UserDetailsService` recupera un utente tramite username e restituisce un `UserDetails`; non confronta direttamente la password. In un flusso DAO, `DaoAuthenticationProvider` usa il `PasswordEncoder` per confrontare la password presentata con il valore codificato. `DelegatingPasswordEncoder` supporta più schemi identificati da un prefisso. L'encoding è one-way e non equivale alla cifratura, che è reversibile.

## Interview answer in English

`UserDetailsService` loads a user by username and returns `UserDetails`; it does not directly validate the password. In DAO authentication, `DaoAuthenticationProvider` delegates password comparison to a `PasswordEncoder`. `DelegatingPasswordEncoder` selects an encoder from the stored prefix. Password encoding is one-way, whereas encryption is reversible with a key.

## Domande di ripasso

1. Quali informazioni espone `UserDetails`?
2. Qual è la responsabilità precisa di `UserDetailsService`?
3. Come collaborano `DaoAuthenticationProvider` e `PasswordEncoder`?
4. Perché `encode(raw).equals(encoded)` non è il confronto corretto?
5. Che informazione porta il prefisso usato da `DelegatingPasswordEncoder`?
6. Perché una password codificata non è una password cifrata?
7. Che authority produce `roles("USER")`?
8. Quale risposta HTTP è stata osservata con credenziali errate?
9. **Who performs the password comparison in DAO authentication?**
10. **What is the difference between `encode` and `matches`?**
11. **Why must raw passwords never be stored?**

## Concetto da ricordare

**`UserDetailsService` carica l'utente; `PasswordEncoder` codifica e confronta; il provider coordina il processo.**
