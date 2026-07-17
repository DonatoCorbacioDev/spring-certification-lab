package com.donatodev.springlab.repository;

import com.donatodev.springlab.dto.response.BookResponse;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository temporaneo in memoria.
 *
 * Non utilizza ancora un database:
 * i dati vengono persi al riavvio dell'applicazione.
 */
@Repository
public class BookRepository {

    private final List<BookResponse> books =
            new ArrayList<>();

    private long nextId = 1L;

    public BookRepository() {
        books.add(new BookResponse(
                nextId++,
                "Clean Code",
                "Robert C. Martin",
                true
        ));

        books.add(new BookResponse(
                nextId++,
                "Effective Java",
                "Joshua Bloch",
                false
        ));

        books.add(new BookResponse(
                nextId++,
                "Spring in Action",
                "Craig Walls",
                true
        ));
    }

    public List<BookResponse> findAll() {
        return List.copyOf(books);
    }

    public Optional<BookResponse> findById(Long id) {
        return books.stream()
                .filter(book -> book.id().equals(id))
                .findFirst();
    }

    public BookResponse save(
            String title,
            String author
    ) {
        BookResponse book = new BookResponse(
                nextId++,
                title,
                author,
                true
        );

        books.add(book);

        return book;
    }
}