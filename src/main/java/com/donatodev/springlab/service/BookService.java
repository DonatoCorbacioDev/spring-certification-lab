package com.donatodev.springlab.service;

import com.donatodev.springlab.dto.request.BookRequest;
import com.donatodev.springlab.dto.response.BookResponse;
import com.donatodev.springlab.entity.BookEntity;
import com.donatodev.springlab.exception.BookNotFoundException;
import com.donatodev.springlab.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Contiene la logica applicativa
 * relativa al catalogo dei libri.
 */
@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(
            BookRepository bookRepository
    ) {
        this.bookRepository = bookRepository;
    }

    public List<BookResponse> findAll() {
        return bookRepository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public BookResponse findById(Long id) {
        BookEntity entity = bookRepository
                .findById(id)
                .orElseThrow(
                        () -> new BookNotFoundException(id)
                );

        return toResponse(entity);
    }

    public BookResponse create(BookRequest request) {
        BookEntity entity = new BookEntity(
                request.title(),
                request.author(),
                request.copies(),
                request.replacementCost(),
                request.category(),
                request.publishedDate()
        );

        BookEntity savedEntity =
                bookRepository.save(entity);

        return toResponse(savedEntity);
    }

    /**
     * Converte il modello persistente
     * nel DTO pubblico dell'API.
     */
    private BookResponse toResponse(
            BookEntity entity
    ) {
        return new BookResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getAuthor(),
                entity.getCopies(),
                entity.getReplacementCost(),
                entity.getCategory(),
                entity.getPublishedDate(),
                entity.getCreatedAt(),
                entity.getCopies() > 0
        );
    }
}