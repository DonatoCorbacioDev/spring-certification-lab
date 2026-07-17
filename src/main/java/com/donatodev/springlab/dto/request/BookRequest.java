package com.donatodev.springlab.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Rappresenta e valida i dati inviati dal client
 * per aggiungere un libro al catalogo.
 */
public record BookRequest(

        @NotBlank(message = "Il titolo è obbligatorio")
        String title,

        @NotBlank(message = "L'autore è obbligatorio")
        String author,

        @NotNull(message = "Il numero di copie è obbligatorio")
        @Positive(message = "Il numero di copie deve essere positivo")
        Integer copies
) {
}