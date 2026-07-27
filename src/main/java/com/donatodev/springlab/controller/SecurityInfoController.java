package com.donatodev.springlab.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Espone gli endpoint usati per osservare autenticazione e autorizzazione.
 *
 * <p>I vincoli di accesso non sono dichiarati nel controller, ma nella
 * {@code SecurityFilterChain}; questo componente rimane quindi il confine HTTP
 * che restituisce dati sull'identità già stabilita da Spring Security.</p>
 */
@RestController
@RequestMapping("/api/security")
public class SecurityInfoController {

    @GetMapping("/public")
    public String getPublicInfo() {
        return "Endpoint pubblico raggiunto";
    }

    /**
     * Espone nome e authority dell'{@link Authentication} recuperata dal
     * {@code SecurityContext} associato alla richiesta corrente.
     *
     * @param authentication identità autenticata
     * @return username e authority dell'utente
     */
    @GetMapping("/me")
    public Map<String, Object> getAuthenticatedUser(Authentication authentication) {

        return Map.of("username", authentication.getName(),
                      "authorities", authentication.getAuthorities()
        );
    }

    @GetMapping("/admin")
    public String getAdminInfo() {
        return "This endpoint is available only to administrators";
    }

    @GetMapping("/user")
    public String getUserInfo() {
        return "This endpoint is available to users and administrators";
    }
}
