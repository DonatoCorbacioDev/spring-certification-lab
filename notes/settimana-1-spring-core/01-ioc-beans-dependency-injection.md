# IoC, Bean e Dependency Injection

## Obiettivo

Comprendere come il container Spring crea gli oggetti applicativi, li registra come **bean** e fornisce le dipendenze necessarie.

## Concetti fondamentali

| Concetto | Significato |
|---|---|
| Bean | Oggetto creato, configurato e gestito dal container Spring |
| IoC | Il controllo della creazione e del collegamento degli oggetti passa al container |
| Dependency Injection | Il container fornisce a un oggetto le dipendenze richieste |
| `ApplicationContext` | Container Spring che conserva bean e relative definizioni |

Una classe Java è un tipo; una sua istanza diventa un bean quando viene gestita da Spring. IoC è il principio generale, Dependency Injection è uno dei meccanismi con cui viene realizzato.

## Flusso interno

```text
Avvio → component scanning → definizioni dei bean
      → creazione dei bean → risoluzione dipendenze
      → constructor injection → applicazione pronta
```

Nel laboratorio:

```text
AppRunner → MemberService → NotificationService → implementazione concreta
```

`MemberService` dipende dal contratto `NotificationService`, non crea direttamente un servizio con `new`.

## Componenti e annotazioni utilizzati

- `ApplicationContext`: gestisce il grafo degli oggetti.
- `@Service`: rende rilevabili i servizi concreti tramite component scanning.
- `@Component`: registra componenti generici come `AppRunner`.
- `NotificationService`: contratto comune.
- `EmailNotificationService`: implementazione concreta.

## Codice essenziale

```java
private final NotificationService notificationService;

public MemberService(NotificationService notificationService) {
    this.notificationService = notificationService;
}
```

Con un solo costruttore, `@Autowired` non è necessario. La constructor injection rende la dipendenza obbligatoria, visibile e assegnabile a un campo `final`.

## Verifiche eseguite

- Creazione di `NotificationService`, `EmailNotificationService`, `MemberService` e `AppRunner`.
- Avvio del contesto senza creazione manuale dei service.
- Esecuzione della registrazione dimostrativa e osservazione della notifica in console.

## Errori e differenze importanti

- Un'interfaccia non è istanziabile: serve almeno un bean concreto compatibile.
- Non ogni service richiede un'interfaccia; è utile quando rappresenta una vera astrazione o più implementazioni.
- Usare `new` dentro `MemberService` lo legherebbe a una specifica implementazione e aggirerebbe il container.
- La Dependency Injection non coincide con IoC: ne è un'applicazione.

## Limiti attuali

Il salvataggio e la notifica sono simulati. Non sono ancora coinvolti database, transazioni o API del catalogo.

## Collegamento a BCM 2.0

Lo stesso principio consente ai service di dipendere da contratti e repository senza costruire direttamente le implementazioni. Il container mantiene il grafo delle dipendenze esplicito e sostituibile.

## Risposta da colloquio in italiano

Spring usa un container IoC per creare, configurare e collegare oggetti chiamati bean. La Dependency Injection è il meccanismo con cui il container fornisce le dipendenze richieste. Nel laboratorio `MemberService` riceve un `NotificationService` tramite costruttore. Preferisco la constructor injection perché rende le dipendenze obbligatorie, esplicite e più semplici da verificare.

## Interview answer in English

Spring uses an IoC container to create, configure, and connect application objects called beans. Dependency Injection is the mechanism used to provide a bean with its required collaborators. In the laboratory, `MemberService` receives a `NotificationService` through constructor injection, keeping the dependency explicit and mandatory.

## Domande di ripasso

1. Che cosa distingue una classe Java da un bean Spring?
2. Che cosa significa Inversion of Control?
3. Qual è il rapporto tra IoC e Dependency Injection?
4. Perché `MemberService` dipende da `NotificationService`?
5. Quale ruolo svolge l'`ApplicationContext`?
6. Perché la constructor injection favorisce campi `final`?
7. Quando un'interfaccia applicativa è realmente utile?
8. Quale problema introdurrebbe `new EmailNotificationService()` nel service?
9. **What is a Spring bean?**
10. **Why is constructor injection preferred over hidden dependencies?**
11. **Who creates `MemberService` in this laboratory?**

## Concetto da ricordare

**IoC affida il controllo al container; Dependency Injection collega i bean senza nascondere le dipendenze.**
