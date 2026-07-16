package com.donatodev.springlab.service;

import org.springframework.stereotype.Service;

/**
 * Implementazione concreta di NotificationService
 * che simula l'invio di una notifica tramite SMS.
 *
 * @Service permette a Spring di rilevare la classe
 * e registrare un suo oggetto come Bean.
 */
@Service
public class SmsNotificationService implements NotificationService {

    /**
     * Implementa il contratto definito
     * dall'interfaccia NotificationService.
     */
    @Override
    public void send(String message) {
        System.out.println("SMS inviato: " + message);
    }
}
