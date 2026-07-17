package com.donatodev.springlab.controller;

import com.donatodev.springlab.dto.response.BookResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Espone gli endpoint REST dimostrativi
 * per consultare il catalogo della biblioteca.
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    @GetMapping
    public List<BookResponse> findAll() {
        return List.of(
                new BookResponse(
                        1L,
                        "Clean Code",
                        "Robert C. Martin",
                        true
                ),
                new BookResponse(
                        2L,
                        "Effective Java",
                        "Joshua Bloch",
                        false
                ),
                new BookResponse(
                        3L,
                        "Spring in Action",
                        "Craig Walls",
                        true
                )
        );
    }

    @GetMapping("/{id}")
    public BookResponse findById(
            @PathVariable Long id
    ) {
        return new BookResponse(
                id,
                "Libro demo " + id,
                "Autore demo",
                true
        );
    }
}