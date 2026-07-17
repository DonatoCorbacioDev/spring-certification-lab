package com.donatodev.springlab.dto.response;

import com.donatodev.springlab.entity.BookCategory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Dati del libro restituiti
 * dalle API REST.
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