package com.donatodev.springlab.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookEntityTest {

    @Test
    void borrowCopyDecreasesCopiesWhenBookIsAvailable() {
        BookEntity book = createBook(2);

        book.borrowCopy();

        assertEquals(1, book.getCopies());
    }

    @Test
    void borrowCopyThrowsWhenNoCopiesAreAvailable() {
        BookEntity book = createBook(0);

        assertThrows(IllegalStateException.class, book::borrowCopy);
    }

    private BookEntity createBook(int copies) {
        return new BookEntity(
                "Effective Java",
                "Joshua Bloch",
                copies,
                new BigDecimal("45.00"),
                BookCategory.TECHNOLOGY,
                LocalDate.of(2018, 1, 6),
                new PublisherEntity("Addison-Wesley")
        );
    }
}
