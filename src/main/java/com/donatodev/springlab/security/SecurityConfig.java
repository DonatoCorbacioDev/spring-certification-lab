package com.donatodev.springlab.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configura le regole di sicurezza HTTP dell'applicazione.
 */
@Configuration
public class SecurityConfig {

    /**
     * Definisce quali richieste possono essere eseguite liberamente
     * e quali richiedono un utente autenticato.
     *
     * @param http configurazione della sicurezza HTTP
     * @return catena di filtri configurata
     * @throws Exception se la configurazione non può essere costruita
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .authorizeHttpRequests(authorize -> authorize
                        // Consente l'accesso senza autenticazione.
                        .requestMatchers("/api/security/public").permitAll()

                        // Tutti gli altri endpoint richiedono autenticazione.
                        .anyRequest().authenticated()
                )
                // Mantiene disponibile la pagina di login predefinita.
                .formLogin(Customizer.withDefaults())

                // Consente anche l'autenticazione HTTP Basic
                // per client come Postman o curl.
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}