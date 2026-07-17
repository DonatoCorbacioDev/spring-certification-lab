package com.donatodev.springlab.runner;

import com.donatodev.springlab.entity.BookCategory;
import com.donatodev.springlab.entity.BookEntity;
import jakarta.persistence.EntityManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Inserisce due libri nel database H2
 * all'avvio dell'applicazione.
 */
@Component
public class BookDataLoader implements CommandLineRunner {

    private final EntityManager entityManager;

    public BookDataLoader(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void run(String... args) {
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

        entityManager.persist(firstBook);
        entityManager.persist(secondBook);

        entityManager.flush();

        System.out.println(
                "Book IDs persisted in H2: "
                        + firstBook.getId()
                        + ", "
                        + secondBook.getId()
        );
    }
}