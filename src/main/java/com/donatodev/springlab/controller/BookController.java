package com.donatodev.springlab.controller;

import com.donatodev.springlab.dto.request.BookRequest;
import com.donatodev.springlab.dto.response.BookResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    public ResponseEntity<BookResponse> create(
            @RequestBody BookRequest request
    ) {
        BookResponse response = new BookResponse(
                4L,
                request.title(),
                request.author(),
                true
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}