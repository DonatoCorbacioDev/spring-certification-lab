package com.donatodev.springlab.dto.response;

import com.donatodev.springlab.entity.BookCategory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO di risposta che espone i dati di un libro senza rendere pubblica
 * l'entity JPA e la sua relazione con l'editore.
 */
public record BookResponse(
        Long id,
        String title,
        String author,
        Integer copies,
        BigDecimal replacementCost,
        BookCategory category,
        LocalDate publishedDate,
        LocalDateTime createdAt,
        boolean available,
        Long publisherId,
        String publisherName
) {
}