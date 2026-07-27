# Constructor injection, Primary e Qualifier

## Obiettivo

Comprendere come Spring risolve una dipendenza quando esistono più bean compatibili con lo stesso tipo.

## Concetti fondamentali

Nel laboratorio due bean implementano `NotificationService`:

```text
NotificationService
├── EmailNotificationService
└── SmsNotificationService
```

Se un injection point richiede soltanto `NotificationService`, il tipo non basta a scegliere.

| Strumento | Funzione |
|---|---|
| `@Primary` | Indica il candidato predefinito tra più bean compatibili |
| `@Qualifier` | Restringe esplicitamente i candidati per uno specifico injection point |
| `@Autowired` | Richiede l'injection; è omissibile su un unico costruttore |

Nel codice corrente `EmailNotificationService` è `@Primary`, ma `MemberService` usa `@Qualifier("smsNotificationService")`: viene quindi iniettato il servizio SMS.

## Flusso interno

```text
Parametro NotificationService
  → ricerca dei bean compatibili
  → applicazione del qualifier, se presente
  → altrimenti candidato @Primary
  → un solo candidato risolto
  → constructor injection
```

Senza criterio risolutivo, l'avvio fallisce per dipendenza non univoca.

## Componenti e annotazioni utilizzati

- `NotificationService`: tipo richiesto.
- `EmailNotificationService`: `@Service` e `@Primary`.
- `SmsNotificationService`: `@Service`.
- `MemberService`: constructor injection con `@Qualifier`.

## Codice essenziale

```java
public MemberService(
        @Qualifier("smsNotificationService")
        NotificationService notificationService,
        MemberRepository memberRepository) {
    ...
}
```

## Verifiche eseguite

- Osservato l'errore di ambiguità con due implementazioni compatibili.
- Risolta la selezione predefinita con `@Primary`.
- Selezionato esplicitamente `SmsNotificationService` con `@Qualifier`.
- Verificato in console il messaggio `SMS inviato`.

## Errori e differenze importanti

- `@Primary` non elimina gli altri bean: stabilisce solo una preferenza.
- `@Qualifier` non cambia lo scope del bean.
- Il nome predefinito deriva normalmente dal nome della classe con iniziale minuscola.
- Un qualifier esplicito restringe la scelta e prevale sul candidato `@Primary` per quel punto di injection.
- La field injection nasconde dipendenze che il costruttore renderebbe obbligatorie.

## Limiti attuali

Email e SMS sono implementazioni simulate; non inviano comunicazioni reali.

## Collegamento a BCM 2.0

`@Primary` e `@Qualifier` sono utili quando più strategie implementano lo stesso contratto. La selezione deve restare esplicita quando il comportamento richiesto dipende dal caso d'uso.

## Risposta da colloquio in italiano

Con la constructor injection Spring risolve i parametri del costruttore cercando bean compatibili. Se ne trova più di uno, `@Primary` indica il candidato predefinito, mentre `@Qualifier` seleziona esplicitamente il bean per uno specifico injection point. Nel laboratorio il qualifier su `MemberService` seleziona il servizio SMS anche se quello email è `@Primary`.

## Interview answer in English

With constructor injection, Spring resolves constructor parameters by type. If multiple candidates exist, `@Primary` defines the default candidate, while `@Qualifier` narrows the selection for a specific injection point. In the laboratory, the qualifier selects the SMS service even though the email service is primary.

## Domande di ripasso

1. Perché due implementazioni dello stesso contratto possono creare ambiguità?
2. Che cosa indica `@Primary`?
3. Che cosa seleziona `@Qualifier`?
4. Quale implementazione riceve oggi `MemberService`?
5. Perché `@Autowired` è omissibile sull'unico costruttore?
6. `@Primary` impedisce la registrazione degli altri bean?
7. Da dove deriva il nome `smsNotificationService`?
8. Quale errore emerge se non esiste un candidato univoco?
9. **How does `@Qualifier` affect candidate selection?**
10. **Does `@Primary` remove other beans from the context?**
11. **Why does constructor injection make dependencies explicit?**

## Concetto da ricordare

**`@Primary` definisce il default; `@Qualifier` esprime la scelta locale e specifica.**
