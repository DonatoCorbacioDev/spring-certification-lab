package com.donatodev.springlab.dto.request;

import com.donatodev.springlab.entity.BookCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;

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
        @Size(max = 200, message = "Il titolo non può superare 200 caratteri")
        String title,

        @NotBlank(message = "L'autore è obbligatorio")
        @Size(max = 150, message = "L'autore non può superare 150 caratteri")
        String author,

        @NotNull(message = "Il numero di copie è obbligatorio")
        @Positive(message = "Il numero di copie deve essere positivo")
        Integer copies,

        @NotNull(message = "Il costo di sostituzione è obbligatorio")
        @Positive(message = "Il costo deve essere positivo")
        @Digits(
                integer = 8,
                fraction = 2,
                message = "Il costo deve avere al massimo 8 cifre intere e 2 decimali"
        )
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
