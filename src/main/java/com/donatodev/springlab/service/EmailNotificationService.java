package com.donatodev.springlab.service;

import org.springframework.stereotype.Service;

/**
 * Implementazione concreta del servizio di notifica.
 *
 * @Service permette a Spring di trovare questa classe
 * tramite il component scanning e di creare un Bean
 * gestito nell'ApplicationContext.
 */
@Service
public class EmailNotificationService
        implements NotificationService {

    /**
     * Simula l'invio di una notifica tramite email.
     */
    @Override
    public void send(String message) {
        System.out.println("Email inviata: " + message);
    }
}