# Component scanning e annotazioni stereotype

## Obiettivo

Comprendere come Spring individua le classi applicative e come le annotazioni comunicano la responsabilità architetturale dei componenti.

## Concetti fondamentali

Il **component scanning** cerca classi candidate nei package configurati e registra le relative definizioni di bean. Con `@SpringBootApplication`, la scansione parte normalmente dal package della classe principale e include i sottopackage.

```text
com.donatodev.springlab
├── controller
├── service
├── repository
└── runner
```

| Annotazione | Ruolo espresso |
|---|---|
| `@Component` | Componente generico |
| `@Service` | Logica o coordinamento applicativo |
| `@Repository` | Accesso ai dati; può partecipare alla traduzione delle eccezioni di persistenza |
| `@Controller` | Componente Spring MVC |
| `@RestController` | `@Controller` + `@ResponseBody`, per risposte HTTP serializzate |

**Correzione tecnica:** `@RestController` non è semplicemente uno stereotype equivalente agli altri: è un'annotazione composta specifica di Spring MVC.

## Flusso interno

```text
@SpringBootApplication
  → scansione del package radice
  → rilevamento delle classi annotate
  → registrazione delle bean definition
  → creazione e injection dei bean
```

Flusso del laboratorio:

```text
Browser → MemberController → MemberService
        → MemberRepository → NotificationService
```

## Componenti e annotazioni utilizzati

- `MemberController`: `@RestController`
- `MemberService`, `EmailNotificationService`, `SmsNotificationService`: `@Service`
- `MemberRepository`: `@Repository`
- `AppRunner`, `BeanInspectorRunner`: `@Component`

## Codice essenziale

```java
@RestController
@RequestMapping("/api/members")
public class MemberController { ... }
```

La collocazione sotto `com.donatodev.springlab` permette alla scansione predefinita di trovare il controller.

## Verifiche eseguite

- Avvio dell'applicazione con la classe principale nel package radice.
- Creazione automatica dei componenti del laboratorio.
- Chiamata a `GET /api/members/register-demo`.
- Osservazione del flusso Controller–Service–Repository–notifica.

## Errori e differenze importanti

- Una classe fuori dall'albero dei package scansionati non viene rilevata automaticamente.
- Le annotazioni non servono solo a “far funzionare Spring”: esprimono responsabilità diverse.
- L'endpoint dimostrativo usa GET per avviare una registrazione, ma un'operazione che modifica dati dovrebbe normalmente usare POST.
- `@Repository` non rende automaticamente persistente una simulazione: `MemberRepository` stampa soltanto un messaggio.

## Limiti attuali

La registrazione degli iscritti è una demo e il repository non usa un database.

## Collegamento a BCM 2.0

Una disposizione coerente dei package permette di rilevare controller, service e repository mantenendo chiari i confini tra API, logica e accesso ai dati.

## Risposta da colloquio in italiano

Il component scanning individua classi candidate nei package configurati e ne registra le definizioni nel container. In Spring Boot la scansione parte normalmente dal package della classe con `@SpringBootApplication`. `@Component`, `@Service` e `@Repository` esprimono ruoli differenti; `@RestController` è invece un'annotazione MVC composta che combina `@Controller` e `@ResponseBody`.

## Interview answer in English

Component scanning discovers candidate classes in configured packages and registers their bean definitions. In Spring Boot, scanning usually starts from the package containing the `@SpringBootApplication` class. Stereotype annotations communicate architectural roles, while `@RestController` is an MVC composed annotation combining `@Controller` and `@ResponseBody`.

## Domande di ripasso

1. Da dove parte normalmente il component scanning in Spring Boot?
2. Perché la classe principale è collocata nel package radice?
3. Quale responsabilità comunica `@Service`?
4. Che cosa aggiunge semanticamente `@Repository`?
5. Da quali annotazioni è composto `@RestController`?
6. Che cosa accade a un componente fuori dai package scansionati?
7. Quale limite presenta `MemberRepository`?
8. Perché GET non è ideale per una registrazione?
9. **What does component scanning register in the container?**
10. **How does `@RestController` differ from a generic stereotype?**
11. **Which laboratory classes are discovered as services?**

## Concetto da ricordare

**La scansione trova i componenti; le annotazioni ne dichiarano il ruolo nel disegno applicativo.**
