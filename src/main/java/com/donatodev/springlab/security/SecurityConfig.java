package com.donatodev.springlab.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Definisce il confine di sicurezza HTTP dell'applicazione.
 *
 * <p>Le regole sono valutate nell'ordine dichiarato: gli endpoint
 * dimostrativi ricevono vincoli specifici, mentre ogni altra richiesta
 * richiede un utente autenticato. Form login e HTTP Basic condividono
 * lo stesso processo di autenticazione basato sugli utenti persistiti.</p>
 */
@Configuration
public class SecurityConfig {

    /**
     * Costruisce la catena applicando prima i matcher specifici e infine
     * la regola generale {@code authenticated()}.
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
                        .requestMatchers("/api/security/public").permitAll()

                        // hasRole("ADMIN") verifica l'authority ROLE_ADMIN.
                        .requestMatchers("/api/security/admin").hasRole("ADMIN")

                        .requestMatchers("/api/security/user")
                        .hasAnyRole("USER", "ADMIN")

                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    /**
     * Fornisce il {@link PasswordEncoder} delegante usato sia per codificare
     * le password iniziali sia per confrontarle durante l'autenticazione.
     *
     * @return encoder delegante per la password
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
