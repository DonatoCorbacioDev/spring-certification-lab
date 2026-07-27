# Utenti persistiti e integrazione con UserDetailsService

## Obiettivo

Sostituire gli utenti in memoria con utenti caricati da H2, mantenendo separati il modello persistente e il modello richiesto da Spring Security.

## Concetti fondamentali

| Componente | Responsabilità |
|---|---|
| `UserRole` | Rappresenta nel dominio i valori `USER` e `ADMIN` |
| `AppUserEntity` | Modello persistente mappato sulla tabella `app_users` |
| `AppUserRepository` | Accesso Spring Data JPA agli utenti |
| `SecurityDataLoader` | Inserisce gli utenti didattici quando la tabella è vuota |
| `DatabaseUserDetailsService` | Adatta `AppUserEntity` a `UserDetails` |
| `PasswordEncoder` | Codifica le password prima del salvataggio e le confronta in autenticazione |

Scelte del mapping:

- `username` è `nullable = false` e `unique = true`;
- `passwordHash` contiene una password già codificata;
- `role` usa `EnumType.STRING`, quindi il database conserva `USER` o `ADMIN`;
- `enabled` viene convertito nel flag `disabled` di `UserDetails`;
- `findByUsername` restituisce `Optional<AppUserEntity>`.

## Flusso interno

```text
username presentato
  → DaoAuthenticationProvider
  → DatabaseUserDetailsService
  → AppUserRepository.findByUsername
  → query su app_users
  → AppUserEntity
  → UserDetails
  → PasswordEncoder.matches
  → Authentication
```

Conversione centrale:

```java
return User.withUsername(appUser.getUsername())
        .password(appUser.getPasswordHash())
        .roles(appUser.getRole().name())
        .disabled(!appUser.isEnabled())
        .build();
```

`roles(...)` aggiunge il prefisso `ROLE_`. Se la query non trova l'utente, il servizio genera `UsernameNotFoundException`.

## Componenti o configurazione utilizzati

Repository:

```java
Optional<AppUserEntity> findByUsername(String username);
```

Loader:

- verifica `appUserRepository.count()`;
- codifica le password didattiche tramite `PasswordEncoder`;
- salva `donato` con `USER` e `admin` con `ADMIN`;
- non reinserisce dati se la tabella contiene già utenti.

Lo stato finale non espone più un bean `InMemoryUserDetailsManager`: Spring Security usa l'implementazione `DatabaseUserDetailsService`.

## Verifiche eseguite

- Osservata la creazione della tabella `app_users`.
- Osservate query SQL di conteggio, inserimento e ricerca per username.
- Verificata la presenza in H2 degli utenti `donato` e `admin`.
- Verificato che i valori persistiti delle password siano codificati e non grezzi.

| Verifica HTTP | Risultato osservato |
|---|---:|
| Credenziali valide su endpoint consentito | **200 OK** |
| Credenziali errate | **401 Unauthorized** |
| `donato` (`ROLE_USER`) su endpoint admin | **403 Forbidden** |
| `admin` (`ROLE_ADMIN`) su endpoint admin | **200 OK** |

## Errori e differenze importanti

- `AppUserEntity` non implementa `UserDetails`: i due modelli hanno responsabilità diverse.
- `DatabaseUserDetailsService` carica e converte l'utente, ma non verifica direttamente la password.
- `passwordHash` non deve ricevere la password grezza.
- `EnumType.STRING` evita di legare il dato persistito alla posizione numerica dell'enum.
- `Optional` rende esplicita l'assenza; il confine security la traduce in `UsernameNotFoundException`.
- Un account con `enabled = false` viene costruito come `disabled` e non può autenticarsi.
- `findByUsername` è una query derivata dal nome del metodo, non una query scritta manualmente.

## Limiti attuali

- H2 è in memoria e la configurazione usa `create-drop`.
- Le credenziali sono hard-coded esclusivamente per il laboratorio.
- Ogni utente possiede un solo ruolo.
- Non esistono registrazione, modifica password o gestione amministrativa degli utenti.
- Non sono presenti utenti esterni, JWT o OAuth2.

## Collegamento a BCM 2.0

Il confine tra entity e modello security è riutilizzabile in BCM 2.0: il dominio può evolvere senza rendere l'entity il contratto di Spring Security. In produzione serviranno persistenza stabile, provisioning sicuro e gestione del ciclo di vita delle credenziali.

## Risposta da colloquio in italiano

Nel laboratorio `AppUserEntity` rappresenta l'utente persistito, mentre `UserDetails` è il modello richiesto da Spring Security. `DatabaseUserDetailsService` cerca l'entity tramite `AppUserRepository.findByUsername`, genera `UsernameNotFoundException` se manca e la converte in `UserDetails`. Il servizio non confronta la password: il provider usa il `PasswordEncoder`. Le password vengono codificate dal loader prima del salvataggio e `roles(...)` converte `USER` in `ROLE_USER`.

## Interview answer in English

In the laboratory, `AppUserEntity` is the persistence model, while `UserDetails` is the model required by Spring Security. `DatabaseUserDetailsService` loads the entity through `findByUsername`, throws `UsernameNotFoundException` when necessary, and maps it to `UserDetails`. It does not compare passwords directly; the authentication provider uses the configured `PasswordEncoder`.

## Domande di ripasso

1. Perché `AppUserEntity` e `UserDetails` restano modelli distinti?
2. Quale query deriva Spring Data da `findByUsername`?
3. Perché il repository restituisce `Optional`?
4. Dove viene tradotta l'assenza dell'utente in `UsernameNotFoundException`?
5. Qual è il vantaggio di `EnumType.STRING`?
6. Come viene convertito `enabled` nel builder di `UserDetails`?
7. In quale momento le password didattiche vengono codificate?
8. Come si verifica dai risultati HTTP che i ruoli provengono dal database?
9. **Which component adapts the persistence model to Spring Security?**
10. **Why must `passwordHash` never contain a raw password?**
11. **What authority does `.roles(appUser.getRole().name())` create for `ADMIN`?**

## Concetto da ricordare

**L'entity conserva i dati; `DatabaseUserDetailsService` li adatta; il provider e il `PasswordEncoder` autenticano.**
