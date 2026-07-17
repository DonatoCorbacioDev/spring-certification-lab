package com.donatodev.springlab.service;

import com.donatodev.springlab.dto.request.BookRequest;
import com.donatodev.springlab.dto.response.BookResponse;
import com.donatodev.springlab.entity.BookCategory;
import com.donatodev.springlab.entity.BookEntity;
import com.donatodev.springlab.entity.PublisherEntity;
import com.donatodev.springlab.exception.BookNotFoundException;
import com.donatodev.springlab.exception.PublisherNotFoundException;
import com.donatodev.springlab.repository.BookRepository;
import com.donatodev.springlab.repository.PublisherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Contiene la logica applicativa relativa al catalogo dei libri.
 *
 * Coordina i repository, delimita le transazioni e converte
 * le entity persistenti nei DTO esposti dall'API.
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
     * Converte un'entity persistente nel DTO pubblico dell'API.
     * Il mapping avviene all'interno della transazione, così la relazione
     * {@code publisher}, configurata con fetch LAZY, può essere inizializzata.
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

    @Transactional(readOnly = true)
    public Page<BookResponse> findByCategory(
            BookCategory category,
            Pageable pageable
    ) {
        return bookRepository
                .findByCategory(category, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<BookResponse> findByPublisher(
            Long publisherId,
            Pageable pageable
    ) {
        return bookRepository
                .findByPublisher_Id(publisherId, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<BookResponse> searchByTitle(
            String text,
            Pageable pageable
    ) {
        return bookRepository
                .searchByTitle(text, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public List<BookResponse> findByAuthor(
            String text,
            Sort sort
    ) {
        return bookRepository
                .findByAuthorContainingIgnoreCase(text, sort)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public BookResponse borrowCopy(Long id) {
        BookEntity book = bookRepository
                .findById(id)
                .orElseThrow(
                        () -> new BookNotFoundException(id)
                );

        book.borrowCopy();

        /*
         * L'entity è gestita dal persistence context: al commit Hibernate
         * rileva la modifica e la persiste tramite dirty checking.
         */

        return toResponse(book);
    }
}