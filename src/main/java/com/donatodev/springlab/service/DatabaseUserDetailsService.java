package com.donatodev.springlab.service;

import com.donatodev.springlab.entity.AppUserEntity;
import com.donatodev.springlab.repository.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Carica dal database gli utenti utilizzati da Spring Security.
 *
 * <p>Questa classe adatta il modello persistente {@link AppUserEntity}
 * al contratto {@link UserDetails} richiesto dal processo
 * di autenticazione di Spring Security.</p>
 *
 * <p>Non verifica direttamente la password: restituisce la password
 * già codificata e lascia il confronto al provider di autenticazione,
 * che utilizzerà il PasswordEncoder configurato.</p>
 */
@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public DatabaseUserDetailsService(
            AppUserRepository appUserRepository) {

        this.appUserRepository = appUserRepository;
    }

    /**
     * Cerca un utente applicativo attraverso lo username e lo converte
     * nel modello {@link UserDetails}. Il builder {@code roles(...)} trasforma
     * {@code USER}/{@code ADMIN} in {@code ROLE_USER}/{@code ROLE_ADMIN};
     * lo stato persistito viene inoltre tradotto nel flag {@code disabled}.
     *
     * @param username username ricevuto durante l'autenticazione
     * @return dati dell'utente nel formato richiesto da Spring Security
     * @throws UsernameNotFoundException quando lo username non esiste
     */
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        AppUserEntity appUser = appUserRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found: " + username
                ));

        return User.withUsername(appUser.getUsername())
                .password(appUser.getPasswordHash())
                .roles(appUser.getRole().name())
                .disabled(!appUser.isEnabled())
                .build();
    }
}
