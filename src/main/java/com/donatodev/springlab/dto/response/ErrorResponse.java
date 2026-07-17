package com.donatodev.springlab.dto.response;

import java.time.LocalDateTime;

/**
 * Rappresenta la risposta JSON restituita
 * quando si verifica un errore nelle API.
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}