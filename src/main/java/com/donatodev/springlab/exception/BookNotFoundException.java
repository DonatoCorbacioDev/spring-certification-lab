package com.donatodev.springlab.exception;

/**
 * Rappresenta il caso in cui
 * un libro richiesto non esiste.
 */
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long id) {
        super("Libro non trovato con id: " + id);
    }
}