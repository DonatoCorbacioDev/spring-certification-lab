package com.donatodev.springlab.service;

/**
 * Definisce il contratto comune delle diverse strategie di notifica,
 * senza vincolare i client a una specifica implementazione.
 */
public interface NotificationService {

    void send(String message);
}
