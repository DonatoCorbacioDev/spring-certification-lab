package com.donatodev.springlab.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Implementazione di {@link NotificationService}
 * che simula l'invio di una notifica via email.
 *
 * {@code @Primary} la rende la scelta predefinita quando Spring
 * deve iniettare un {@code NotificationService} senza qualifier.
 */
@Service
@Primary
public class EmailNotificationService
        implements NotificationService {

    @Override
    public void send(String message) {
        System.out.println("Email inviata: " + message);
    }
}