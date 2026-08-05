package com.donatodev.springlab.repository;

import com.donatodev.springlab.entity.BookCategory;
import com.donatodev.springlab.entity.BookEntity;
import com.donatodev.springlab.entity.PublisherEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private EntityManager entityManager;

    private PublisherEntity addisonWesley;
    private PublisherEntity demoPress;

    @BeforeEach
    void setUp() {
        addisonWesley = publisherRepository.save(
                new PublisherEntity("Addison-Wesley")
        );
        demoPress = publisherRepository.save(
                new PublisherEntity("Demo Press")
        );

        bookRepository.saveAll(List.of(
                createBook("Effective Java", "Joshua Bloch", BookCategory.TECHNOLOGY, addisonWesley),
                createBook("Domain-Driven Design", "Eric Evans", BookCategory.TECHNOLOGY, addisonWesley),
                createBook("Java Stories", "Demo Author", BookCategory.FICTION, demoPress),
                createBook("Spring in Practice", "Demo Author", BookCategory.TECHNOLOGY, demoPress)
        ));
        bookRepository.flush();
        entityManager.clear();
    }

    @Test
    void findByCategoryReturnsOrderedPageAndMetadata() {
        Page<BookEntity> page = bookRepository.findByCategory(
                BookCategory.TECHNOLOGY,
                PageRequest.of(0, 2, Sort.by("title").ascending())
        );

        assertEquals(List.of("Domain-Driven Design", "Effective Java"),
                page.getContent().stream().map(BookEntity::getTitle).toList());
        assertEquals(3, page.getTotalElements());
        assertEquals(2, page.getTotalPages());
    }

    @Test
    void findByPublisherIdReturnsOnlyPublisherBooks() {
        Page<BookEntity> page = bookRepository.findByPublisher_Id(
                addisonWesley.getId(), PageRequest.of(0, 10)
        );

        assertEquals(2, page.getTotalElements());
        assertEquals(List.of("Domain-Driven Design", "Effective Java"),
                page.getContent().stream()
                        .map(BookEntity::getTitle)
                        .sorted()
                        .toList());
    }

    @Test
    void searchByTitleIsCaseInsensitive() {
        Page<BookEntity> page = bookRepository.searchByTitle(
                "JAVA", PageRequest.of(0, 10, Sort.by("title"))
        );

        assertEquals(List.of("Effective Java", "Java Stories"),
                page.getContent().stream().map(BookEntity::getTitle).toList());
    }

    @Test
    void findByAuthorContainingIgnoreCaseAppliesSort() {
        List<BookEntity> books = bookRepository.findByAuthorContainingIgnoreCase(
                "demo", Sort.by("title").descending()
        );

        assertEquals(List.of("Spring in Practice", "Java Stories"),
                books.stream().map(BookEntity::getTitle).toList());
    }

    @Test
    void persistedBookRetainsPublisherForeignKey() {
        BookEntity book = bookRepository.searchByTitle(
                "Effective Java", PageRequest.of(0, 1)
        ).getContent().getFirst();

        assertNotNull(book.getId());
        assertEquals(addisonWesley.getId(), book.getPublisher().getId());
        assertEquals("Addison-Wesley", book.getPublisher().getName());
    }

    private BookEntity createBook(
            String title,
            String author,
            BookCategory category,
            PublisherEntity publisher
    ) {
        return new BookEntity(
                title,
                author,
                2,
                new BigDecimal("39.90"),
                category,
                LocalDate.of(2020, 1, 1),
                publisher
        );
    }
}
