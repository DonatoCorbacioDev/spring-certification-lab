package com.donatodev.springlab.runner;

import com.donatodev.springlab.entity.AppUserEntity;
import com.donatodev.springlab.entity.UserRole;
import com.donatodev.springlab.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Inserisce nel database gli utenti didattici necessari
 * per il laboratorio Spring Security.
 *
 * <p>I valori di accesso sono casuali, effimeri e codificati tramite
 * {@link PasswordEncoder} prima della persistenza. Non vengono esposti
 * né conservati nel codice sorgente.</p>
 *
 * <p>Questo caricamento iniziale serve esclusivamente al laboratorio.
 * In un'applicazione reale gli utenti non verrebbero normalmente
 * creati in questo modo, ma tramite un processo di provisioning.</p>
 */
@Component
public class SecurityDataLoader implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public SecurityDataLoader(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder) {

        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Inserisce gli utenti iniziali soltanto quando la tabella è vuota,
     * codificando valori effimeri prima della persistenza.
     *
     * @param args argomenti ricevuti all'avvio dell'applicazione
     */
    @Override
    public void run(String... args) {

        if (appUserRepository.count() > 0) {
            return;
        }

        AppUserEntity donato = new AppUserEntity(
                "donato",
                passwordEncoder.encode(UUID.randomUUID().toString()),
                UserRole.USER,
                true
        );

        AppUserEntity admin = new AppUserEntity(
                "admin",
                passwordEncoder.encode(UUID.randomUUID().toString()),
                UserRole.ADMIN,
                true
        );

        appUserRepository.saveAll(List.of(donato, admin));
    }
}
