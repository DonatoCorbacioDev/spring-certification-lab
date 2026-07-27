package com.donatodev.springlab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Rappresenta un utente applicativo persistito nel database.
 *
 * <p>Questa Entity appartiene al modello del laboratorio e rimane distinta
 * dal modello {@code UserDetails} usato internamente da Spring Security.
 * La conversione tra i due modelli sarà responsabilità del
 * {@code DatabaseUserDetailsService}.</p>
 *
 * <p>La password non viene conservata in chiaro: {@code passwordHash}
 * contiene esclusivamente il valore prodotto dal {@code PasswordEncoder}.</p>
 */
@Entity
@Table(name = "app_users")
public class AppUserEntity {

    /**
     * Identificatore generato dal database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome utilizzato per individuare univocamente l'utente durante
     * l'autenticazione.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Password codificata tramite PasswordEncoder.
     * Non deve mai contenere la password originale in chiaro.
     */
    @Column(nullable = false)
    private String passwordHash;

    /**
     * Ruolo applicativo persistito come testo, per esempio USER o ADMIN.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    /**
     * Indica se l'account può essere utilizzato per autenticarsi.
     */
    @Column(nullable = false)
    private boolean enabled;

    /**
     * Costruttore richiesto da JPA.
     */
    protected AppUserEntity() {
    }

    /**
     * Crea un nuovo utente applicativo.
     *
     * @param username nome univoco usato per l'autenticazione
     * @param passwordHash password già codificata
     * @param role ruolo applicativo dell'utente
     * @param enabled stato di abilitazione dell'account
     */
    public AppUserEntity(
            String username,
            String passwordHash,
            UserRole role,
            boolean enabled) {

        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isEnabled() {
        return enabled;
    }
}