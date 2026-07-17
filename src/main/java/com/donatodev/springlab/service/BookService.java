package com.donatodev.springlab.service;

import com.donatodev.springlab.dto.request.BookRequest;
import com.donatodev.springlab.dto.response.BookResponse;
import com.donatodev.springlab.entity.BookEntity;
import com.donatodev.springlab.entity.PublisherEntity;
import com.donatodev.springlab.exception.BookNotFoundException;
import com.donatodev.springlab.exception.PublisherNotFoundException;
import com.donatodev.springlab.repository.BookRepository;
import com.donatodev.springlab.repository.PublisherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Contiene la logica applicativa
 * relativa al catalogo dei libri.
 */
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;

    public BookService(
            BookRepository bookRepository,
            PublisherRepository publisherRepository
    ) {
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
    }

    @Transactional(readOnly = true)
    public List<BookResponse> findAll() {
        return bookRepository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookResponse findById(Long id) {
        BookEntity entity = bookRepository
                .findById(id)
                .orElseThrow(
                        () -> new BookNotFoundException(id)
                );

        return toResponse(entity);
    }

    @Transactional
    public BookResponse create(BookRequest request) {
        PublisherEntity publisher = publisherRepository
                .findById(request.publisherId())
                .orElseThrow(
                        () -> new PublisherNotFoundException(
                                request.publisherId()
                        )
                );

        BookEntity entity = new BookEntity(
                request.title(),
                request.author(),
                request.copies(),
                request.replacementCost(),
                request.category(),
                request.publishedDate(),
                publisher
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
                entity.getCopies() > 0,
                entity.getPublisher().getId(),
                entity.getPublisher().getName()
        );
    }
}