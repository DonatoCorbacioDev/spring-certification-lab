package com.donatodev.springlab.service;

/**
 * Definisce il contratto comune per i servizi di notifica.
 *
 * Un'interfaccia stabilisce quali operazioni devono offrire
 * le implementazioni concrete, senza decidere come vengono eseguite-
 */
public interface NotificationService {

    void send(String message);
}
