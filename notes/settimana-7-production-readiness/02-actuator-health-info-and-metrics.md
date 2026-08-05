# Actuator: health, info e metrics

## Obiettivo

Esporre soltanto endpoint operativi necessari e applicare autorizzazioni esplicite.

## Concetti e flusso

L'allowlist HTTP contiene `health`, `info` e `metrics`. Health è pubblico ma mostra dettagli solo agli autorizzati; ogni altro endpoint Actuator richiede `ROLE_ADMIN`.

```text
/actuator/health -> permitAll
/actuator/**     -> ROLE_ADMIN
anyRequest       -> authenticated
```

## Test eseguiti

- health anonimo 200;
- info con USER 403;
- info con ADMIN 200 e metadati applicativi.

## Errori e limiti

Non sono esposti `env`, `configprops`, `beans`, dump o `loggers`. Le informazioni pubblicate sono nome e descrizione non sensibili.

## Collegamento a BCM 2.0

Una allowlist operativa riduce la superficie informativa mantenendo health check e metriche disponibili agli attori corretti.

## Risposta IT/EN

IT: configuro prima l'esposizione e poi l'autorizzazione, perché sono controlli distinti. EN: endpoint exposure and request authorization are separate controls and both must be restrictive.

## Domande

1. Che differenza c'è tra esposizione e autorizzazione?
2. Perché health può essere pubblico?
3. Che cosa significa `when_authorized`?
4. Perché i matcher specifici precedono `anyRequest`?
5. Perché non esporre `env`?
6. Chi può leggere metrics?
7. Quali dati contiene info?
8. **Why use an explicit exposure allowlist?**
