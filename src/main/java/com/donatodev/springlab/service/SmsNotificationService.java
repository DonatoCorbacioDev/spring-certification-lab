package com.donatodev.springlab.service;

import org.springframework.stereotype.Service;

/**
 * Implementazione di {@link NotificationService}
 * che simula l'invio di una notifica via SMS.
 */
@Service
public class SmsNotificationService implements NotificationService {

    @Override
    public void send(String message) {
        System.out.println("SMS inviato: " + message);
    }
}
