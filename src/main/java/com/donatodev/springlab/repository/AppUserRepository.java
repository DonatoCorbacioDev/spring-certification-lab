package com.donatodev.springlab.repository;

import com.donatodev.springlab.entity.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Fornisce l'accesso persistente agli utenti usati per l'autenticazione.
 */
public interface AppUserRepository extends JpaRepository<AppUserEntity, Long> {

    /**
     * Deriva una query per lo username, vincolato come univoco dalla entity.
     *
     * @param username identificativo presentato durante l'autenticazione
     * @return utente corrispondente, oppure un {@link Optional} vuoto
     */
    Optional<AppUserEntity> findByUsername(String username);
}
