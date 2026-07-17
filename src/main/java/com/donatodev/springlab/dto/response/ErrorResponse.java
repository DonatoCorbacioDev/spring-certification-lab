package com.donatodev.springlab.dto.response;

import java.time.LocalDateTime;

/**
 * DTO di risposta usato per rappresentare in modo uniforme
 * gli errori gestiti dalle API REST.
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}