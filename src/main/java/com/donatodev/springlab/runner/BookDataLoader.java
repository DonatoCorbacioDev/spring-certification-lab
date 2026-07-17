package com.donatodev.springlab.runner;

import com.donatodev.springlab.entity.BookCategory;
import com.donatodev.springlab.entity.BookEntity;
import com.donatodev.springlab.entity.PublisherEntity;
import com.donatodev.springlab.repository.BookRepository;
import com.donatodev.springlab.repository.PublisherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Inserisce alcuni libri iniziali
 * usando Spring Data JPA.
 */
@Component
public class BookDataLoader implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;

    public BookDataLoader(
            BookRepository bookRepository,
            PublisherRepository publisherRepository
    ) {
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
    }

    @Override
    public void run(String... args) {
        if (bookRepository.count() > 0) {
            return;
        }

        PublisherEntity firstPublisher =
                publisherRepository.save(
                        new PublisherEntity("Prentice Hall")
                );

        PublisherEntity secondPublisher =
                publisherRepository.save(
                        new PublisherEntity("Addison-Wesley")
                );

        BookEntity firstBook = new BookEntity(
                "Clean Code",
                "Robert C. Martin",
                2,
                new BigDecimal("45.90"),
                BookCategory.TECHNOLOGY,
                LocalDate.of(2008, 8, 1),
                firstPublisher
        );

        BookEntity secondBook = new BookEntity(
                "Effective Java",
                "Joshua Bloch",
                1,
                new BigDecimal("52.50"),
                BookCategory.TECHNOLOGY,
                LocalDate.of(2018, 1, 6),
                secondPublisher
        );

        bookRepository.saveAll(
                List.of(firstBook, secondBook)
        );
    }
}