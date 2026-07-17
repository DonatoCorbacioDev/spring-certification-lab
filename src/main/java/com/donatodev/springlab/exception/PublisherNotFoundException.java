package com.donatodev.springlab.exception;

/**
 * Indica che l'editore richiesto
 * non esiste nel database.
 */
public class PublisherNotFoundException extends RuntimeException {

    public PublisherNotFoundException(Long id) {
        super("Editore non trovato con id: " + id);
    }
}