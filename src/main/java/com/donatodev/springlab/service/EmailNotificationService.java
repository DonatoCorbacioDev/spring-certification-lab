package com.donatodev.springlab.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Implementazione concreta del servizio di notifica.
 *
 * @Service permette a Spring di trovare questa classe
 * tramite il component scanning e di creare un Bean
 * gestito nell'ApplicationContext.
 *
 * @Primary indica a Spring di scegliere questo Bean
 * quando esistono più implementazioni dello stesso tipo
 * e non viene specificata una scelta più precisa
 */
@Service
@Primary
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