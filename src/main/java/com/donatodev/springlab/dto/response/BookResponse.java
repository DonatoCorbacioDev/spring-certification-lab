package com.donatodev.springlab.dto.response;

/**
 * Rappresenta i dati di un libro
 * restituiti al client tramite API REST.
 */
public record BookResponse(
        Long id,
        String title,
        String author,
        Integer copies,
        boolean available
) {
}