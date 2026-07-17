package com.donatodev.springlab.controller;

import com.donatodev.springlab.dto.request.BookRequest;
import com.donatodev.springlab.dto.response.BookResponse;
import com.donatodev.springlab.entity.BookCategory;
import com.donatodev.springlab.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Gestisce le richieste HTTP relative ai libri
 * e delega la logica al BookService.
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(
            BookService bookService
    ) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookResponse> findAll() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public BookResponse findById(
            @PathVariable Long id
    ) {
        return bookService.findById(id);
    }

    @PostMapping
    public ResponseEntity<BookResponse> create(
            @Valid @RequestBody BookRequest request
    ) {
        BookResponse response =
                bookService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/search/by-category")
    public Page<BookResponse> findByCategory(
            @RequestParam BookCategory category,
            @PageableDefault(
                    size = 2,
                    sort = "title"
            )
            Pageable pageable
    ) {
        return bookService.findByCategory(
                category,
                pageable
        );
    }

    @GetMapping("/search/by-publisher")
    public Page<BookResponse> findByPublisher(
            @RequestParam Long publisherId,
            @PageableDefault(
                    size = 5,
                    sort = "createdAt"
            )
            Pageable pageable
    ) {
        return bookService.findByPublisher(
                publisherId,
                pageable
        );
    }

    @GetMapping("/search/by-title")
    public Page<BookResponse> searchByTitle(
            @RequestParam String text,
            @PageableDefault(
                    size = 5,
                    sort = "title"
            )
            Pageable pageable
    ) {
        return bookService.searchByTitle(
                text,
                pageable
        );
    }

    @GetMapping("/search/by-author")
    public List<BookResponse> findByAuthor(
            @RequestParam String text,
            @RequestParam(
                    defaultValue = "asc"
            )
            String direction
    ) {
        Sort sort;

        if ("desc".equalsIgnoreCase(direction)) {
            sort = Sort.by("title").descending();
        } else {
            sort = Sort.by("title").ascending();
        }

        return bookService.findByAuthor(
                text,
                sort
        );
    }
}