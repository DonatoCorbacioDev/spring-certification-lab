package com.donatodev.springlab.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Espone endpoint dimostrativi utilizzati per verificare
 * il comportamento di Spring Security
 */
@RestController
@RequestMapping("/api/security")
public class SecurityInfoController {

    /**
     * Restituisce un messaggio dimostrativo.
     *
     * Il nome dell'endpoint non lo rende automaticamente pubblico:
     * l'accesso deve essere autorizzato nella configurazione di sicurezza.
     *
     * @return messaggio di conferma
     */
    @GetMapping("/public")
    public String getPublicInfo() {
        return "Endpoint pubblico raggiunto";
    }

    /**
     * Restituisce le informazioni essenziali dell'utente autenticato.
     *
     * Spring Security recupera l'oggetto Authentication
     * dal SecurityContext associato alla richiesta corrente.
     *
     * @param authentication identità autenticata
     * @return username e autorizzazione dell'utente
     */
    @GetMapping("/me")
    public Map<String, Object> getAuthenticatedUser(Authentication authentication) {

        return Map.of("username", authentication.getName(),
                      "authorities", authentication.getAuthorities()
        );
    }
}
