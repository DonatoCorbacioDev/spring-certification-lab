package com.donatodev.springlab.dto.request;

import com.donatodev.springlab.entity.BookCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO di richiesta contenente i dati ricevuti dal client
 * per creare un nuovo libro.
 *
 * I vincoli Bean Validation proteggono il confine HTTP
 * prima che la richiesta raggiunga il service.
 */
public record BookRequest(

        @NotBlank(message = "Il titolo è obbligatorio")
        String title,

        @NotBlank(message = "L'autore è obbligatorio")
        String author,

        @NotNull(message = "Il numero di copie è obbligatorio")
        @Positive(message = "Il numero di copie deve essere positivo")
        Integer copies,

        @NotNull(message = "Il costo di sostituzione è obbligatorio")
        @Positive(message = "Il costo deve essere positivo")
        BigDecimal replacementCost,

        @NotNull(message = "La categoria è obbligatoria")
        BookCategory category,

        @NotNull(message = "La data di pubblicazione è obbligatoria")
        LocalDate publishedDate,

        @NotNull(message = "L'editore è obbligatorio")
        @Positive(message = "L'identificativo dell'editore deve essere positivo")
        Long publisherId
) {
}