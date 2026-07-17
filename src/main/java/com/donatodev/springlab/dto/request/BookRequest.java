package com.donatodev.springlab.dto.request;

/**
 * Rappresenta i dati inviati dal client
 * per aggiungere un libro al catalogo.
 */
public record BookRequest(
        String title,
        String author
) {
}