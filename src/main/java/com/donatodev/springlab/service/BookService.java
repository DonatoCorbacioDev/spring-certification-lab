package com.donatodev.springlab.service;

import com.donatodev.springlab.dto.request.BookRequest;
import com.donatodev.springlab.dto.response.BookResponse;
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
        return bookRepository.findAll();
    }

    public BookResponse findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(
                        () -> new BookNotFoundException(id)
                );
    }

    public BookResponse create(BookRequest request) {
        return bookRepository.save(
                request.title(),
                request.author(),
                request.copies()
        );
    }
}