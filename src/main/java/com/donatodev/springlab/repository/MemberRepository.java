package com.donatodev.springlab.repository;

import org.springframework.stereotype.Repository;

/**
 * Simula il componente responsabile dell'accesso ai dati.
 *
 * @Repository permette a Spring di trovare la classe
 * tramite component scanning e registrarla come Bean.
 *
 * In questo esercizio non utilizziamo ancora un database:
 * stampiamo soltanto il risultato nella console.
 */
@Repository
public class MemberRepository {

    /**
     * Simula il salvataggio di un iscritto.
     */
    public void save(String memberName) {
        System.out.println(
                "Iscritto salvato nel repository: " + memberName
        );
    }
}
