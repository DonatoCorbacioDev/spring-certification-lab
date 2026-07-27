package com.donatodev.springlab.runner;

import com.donatodev.springlab.entity.AppUserEntity;
import com.donatodev.springlab.entity.UserRole;
import com.donatodev.springlab.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Inserisce nel database gli utenti didattici necessari
 * per il laboratorio Spring Security.
 *
 * <p>Le password vengono codificate tramite {@link PasswordEncoder}
 * prima che le Entity siano salvate. Nel database non viene quindi
 * memorizzata la password originale in chiaro.</p>
 *
 * <p>Questo caricamento iniziale serve esclusivamente al laboratorio.
 * In un'applicazione reale gli utenti non verrebbero normalmente
 * creati con credenziali hard-coded nel codice sorgente.</p>
 */
@Component
public class SecurityDataLoader implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Riceve tramite constructor injection il repository degli utenti
     * e il componente responsabile della codifica delle password.
     *
     * @param appUserRepository repository degli utenti applicativi
     * @param passwordEncoder componente per la codifica delle password
     */
    public SecurityDataLoader(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder) {

        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Inserisce gli utenti iniziali soltanto quando la tabella è vuota.
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
                passwordEncoder.encode("Password123!"),
                UserRole.USER,
                true
        );

        AppUserEntity admin = new AppUserEntity(
                "admin",
                passwordEncoder.encode("Admin123!"),
                UserRole.ADMIN,
                true
        );

        appUserRepository.saveAll(List.of(donato, admin));
    }
}