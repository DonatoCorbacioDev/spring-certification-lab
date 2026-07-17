package com.donatodev.springlab.runner;

import com.donatodev.springlab.entity.BookCategory;
import com.donatodev.springlab.entity.BookEntity;
import com.donatodev.springlab.repository.BookRepository;
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

    public BookDataLoader(
            BookRepository bookRepository
    ) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) {
        if (bookRepository.count() > 0) {
            return;
        }

        BookEntity firstBook = new BookEntity(
                "Clean Code",
                "Robert C. Martin",
                2,
                new BigDecimal("45.90"),
                BookCategory.TECHNOLOGY,
                LocalDate.of(2008, 8, 1)
        );

        BookEntity secondBook = new BookEntity(
                "Effective Java",
                "Joshua Bloch",
                1,
                new BigDecimal("52.50"),
                BookCategory.TECHNOLOGY,
                LocalDate.of(2018, 1, 6)
        );

        List<BookEntity> savedBooks =
                bookRepository.saveAll(
                        List.of(firstBook, secondBook)
                );

        System.out.println(
                "Book IDs persisted via JpaRepository: "
                        + savedBooks.stream()
                        .map(BookEntity::getId)
                        .toList()
        );
    }
}