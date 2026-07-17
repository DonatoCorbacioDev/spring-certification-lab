package com.donatodev.springlab.exception;

/**
 * Segnala che l'editore richiesto non esiste.
 */
public class PublisherNotFoundException extends RuntimeException {

    public PublisherNotFoundException(Long id) {
        super("Editore non trovato con id: " + id);
    }
}