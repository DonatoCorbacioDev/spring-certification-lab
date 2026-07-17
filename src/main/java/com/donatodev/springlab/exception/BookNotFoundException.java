package com.donatodev.springlab.exception;

/**
 * Segnala che il libro richiesto non esiste.
 */
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long id) {
        super("Libro non trovato con id: " + id);
    }
}