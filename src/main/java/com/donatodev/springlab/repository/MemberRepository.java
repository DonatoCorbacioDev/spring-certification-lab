package com.donatodev.springlab.repository;

import org.springframework.stereotype.Repository;

/**
 * Simula il layer di accesso ai dati per gli iscritti.
 *
 * Questo esercizio non usa ancora una persistenza reale:
 * il salvataggio viene rappresentato da un messaggio in console.
 */
@Repository
public class MemberRepository {

    public void save(String memberName) {
        System.out.println(
                "Iscritto salvato nel repository: " + memberName
        );
    }
}
