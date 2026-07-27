# ApplicationContext, ispezione dei bean e scope singleton

## Obiettivo

Osservare i bean registrati nell'`ApplicationContext` e verificare il significato dello scope singleton predefinito.

## Concetti fondamentali

L'`ApplicationContext` conserva le definizioni dei bean, ne coordina creazione e dipendenze e consente di recuperarli.

| Operazione | Risultato |
|---|---|
| `getBeanDefinitionNames()` | Nomi delle definizioni note al container |
| `getBean(MemberService.class)` | Bean compatibile con il tipo richiesto |
| `getBeansOfType(NotificationService.class)` | Mappa dei bean compatibili |

Lo scope **singleton** di Spring significa una singola istanza per nome di bean all'interno di uno specifico container. Non coincide con il Singleton pattern implementato dalla classe e non implica un'unica istanza assoluta nella JVM.

## Flusso interno

```text
ApplicationContext
  → registra bean definition
  → crea il singleton quando previsto
  → conserva l'istanza
  → restituisce lo stesso riferimento alle richieste successive
```

## Componenti e annotazioni utilizzati

- `ApplicationContext`
- `BeanInspectorRunner`
- `MemberService`
- `NotificationService` con implementazioni email e SMS
- scope singleton predefinito, senza `@Scope` esplicito

## Codice essenziale

```java
MemberService first = applicationContext.getBean(MemberService.class);
MemberService second = applicationContext.getBean(MemberService.class);

System.out.println(first == second);
```

## Verifiche eseguite

- Filtrati i nomi dei bean del laboratorio tra quelli auto-configurati.
- Recuperato due volte `MemberService`.
- Risultato osservato: `MemberService è la stessa istanza? true`.
- Recuperate entrambe le implementazioni di `NotificationService`.

## Errori e differenze importanti

- **Correzione tecnica:** lo scope singleton garantisce una istanza per bean e per `ApplicationContext`, non necessariamente una sola istanza della classe nell'intera JVM.
- `getBean()` usa il container; `new` crea un oggetto esterno alla sua gestione.
- `getBeansOfType()` mostra tutti i candidati, indipendentemente da `@Primary`.
- Un service singleton può essere usato da più richieste: dati specifici della chiamata non devono essere conservati in campi mutabili condivisi.
- I campi `final` che rappresentano dipendenze non costituiscono stato utente.

## Limiti attuali

Il laboratorio osserva solo lo scope predefinito e non confronta altri scope.

## Collegamento a BCM 2.0

Controller, service e repository saranno normalmente bean singleton. I dati del singolo caso d'uso devono viaggiare tramite parametri e modelli, evitando stato mutabile condiviso nei service.

## Risposta da colloquio in italiano

L'`ApplicationContext` è il container Spring che registra, crea e collega i bean. Posso ispezionarlo tramite i nomi delle definizioni o recuperare bean per tipo. Lo scope singleton indica che uno specifico container riusa la stessa istanza di un bean; è diverso dal Singleton pattern. Per questo i service singleton dovrebbero evitare stato mutabile legato a una richiesta.

## Interview answer in English

The `ApplicationContext` is the Spring container that registers, creates, and wires beans. A singleton-scoped bean has one shared instance per bean definition within a specific container; this is different from the Singleton design pattern. Singleton services should therefore avoid mutable request-specific state.

## Domande di ripasso

1. Quali responsabilità ha l'`ApplicationContext`?
2. Che cosa restituisce `getBeanDefinitionNames()`?
3. Che differenza c'è tra `getBean` e `new`?
4. Che cosa significa realmente scope singleton?
5. Perché non coincide con il Singleton pattern?
6. Che cosa dimostra il confronto `first == second`?
7. Perché un service singleton dovrebbe essere stateless?
8. Che cosa restituisce `getBeansOfType` nel laboratorio?
9. **Is a Spring singleton unique across the entire JVM?**
10. **Why is mutable request state unsafe in a singleton service?**
11. **What did the bean inspection verify?**

## Concetto da ricordare

**Il singleton Spring è condiviso nel container: le dipendenze possono essere campi, lo stato della richiesta no.**
