package com.donatodev.springlab.runner;

import com.donatodev.springlab.entity.BookCategory;
import com.donatodev.springlab.entity.BookEntity;
import com.donatodev.springlab.entity.PublisherEntity;
import com.donatodev.springlab.repository.BookRepository;
import com.donatodev.springlab.repository.PublisherRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Inserisce editori e libri iniziali
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
    public void run(String @NonNull ... args) {

        /*
         * Evita di reinserire i dati
         * se il database contiene già libri.
         */
        if (bookRepository.count() > 0) {
            return;
        }

        /*
         * Prima salviamo gli editori,
         * così otteniamo PublisherEntity persistenti
         * con un identificativo generato.
         */
        PublisherEntity prenticeHall =
                publisherRepository.save(
                        new PublisherEntity("Prentice Hall")
                );

        PublisherEntity addisonWesley =
                publisherRepository.save(
                        new PublisherEntity("Addison-Wesley")
                );

        PublisherEntity demoPress =
                publisherRepository.save(
                        new PublisherEntity("Demo Press")
                );

        /*
         * Creiamo abbastanza libri per osservare:
         *
         * - paginazione;
         * - filtro per categoria;
         * - filtro per editore;
         * - ricerca per titolo;
         * - ricerca per autore;
         * - ordinamento.
         */
        List<BookEntity> books = List.of(

                new BookEntity(
                        "Clean Code",
                        "Robert C. Martin",
                        2,
                        new BigDecimal("45.90"),
                        BookCategory.TECHNOLOGY,
                        LocalDate.of(2008, 8, 1),
                        prenticeHall
                ),

                new BookEntity(
                        "Effective Java",
                        "Joshua Bloch",
                        1,
                        new BigDecimal("52.50"),
                        BookCategory.TECHNOLOGY,
                        LocalDate.of(2018, 1, 6),
                        addisonWesley
                ),

                new BookEntity(
                        "Refactoring",
                        "Martin Fowler",
                        3,
                        new BigDecimal("49.90"),
                        BookCategory.TECHNOLOGY,
                        LocalDate.of(2018, 11, 19),
                        addisonWesley
                ),

                new BookEntity(
                        "Domain-Driven Design",
                        "Eric Evans",
                        1,
                        new BigDecimal("59.90"),
                        BookCategory.TECHNOLOGY,
                        LocalDate.of(2003, 8, 30),
                        addisonWesley
                ),

                new BookEntity(
                        "Java Concurrency in Practice",
                        "Brian Goetz",
                        2,
                        new BigDecimal("54.90"),
                        BookCategory.TECHNOLOGY,
                        LocalDate.of(2006, 5, 19),
                        addisonWesley
                ),

                new BookEntity(
                        "Il codice perduto",
                        "Autore Demo",
                        4,
                        new BigDecimal("19.90"),
                        BookCategory.FICTION,
                        LocalDate.of(2020, 5, 10),
                        demoPress
                ),

                new BookEntity(
                        "Storia dell'informatica",
                        "Autore Demo",
                        2,
                        new BigDecimal("29.90"),
                        BookCategory.HISTORY,
                        LocalDate.of(2021, 3, 12),
                        demoPress
                )
        );

        bookRepository.saveAll(books);

        System.out.println(
                "Dati iniziali caricati: "
                        + publisherRepository.count()
                        + " editori e "
                        + bookRepository.count()
                        + " libri"
        );
    }
}