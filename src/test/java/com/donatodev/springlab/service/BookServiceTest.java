package com.donatodev.springlab.service;

import com.donatodev.springlab.dto.request.BookRequest;
import com.donatodev.springlab.dto.response.BookResponse;
import com.donatodev.springlab.entity.BookCategory;
import com.donatodev.springlab.entity.BookEntity;
import com.donatodev.springlab.entity.PublisherEntity;
import com.donatodev.springlab.exception.BookNotFoundException;
import com.donatodev.springlab.exception.BookNotAvailableException;
import com.donatodev.springlab.exception.PublisherNotFoundException;
import com.donatodev.springlab.repository.BookRepository;
import com.donatodev.springlab.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    private static final Long BOOK_ID = 42L;
    private static final Long PUBLISHER_ID = 7L;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PublisherRepository publisherRepository;

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookService(bookRepository, publisherRepository);
    }

    @Test
    void findByIdReturnsMappedResponseWhenBookExists() {
        BookEntity book = createBook(2, new PublisherEntity("Addison-Wesley"));
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));

        BookResponse response = bookService.findById(BOOK_ID);

        assertNull(response.id());
        assertEquals("Effective Java", response.title());
        assertEquals("Joshua Bloch", response.author());
        assertEquals(2, response.copies());
        assertEquals(new BigDecimal("45.00"), response.replacementCost());
        assertEquals(BookCategory.TECHNOLOGY, response.category());
        assertEquals(LocalDate.of(2018, 1, 6), response.publishedDate());
        assertNotNull(response.createdAt());
        assertTrue(response.available());
        assertNull(response.publisherId());
        assertEquals("Addison-Wesley", response.publisherName());
        verify(bookRepository).findById(BOOK_ID);
    }

    @Test
    void findByIdThrowsWhenBookDoesNotExist() {
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.empty());

        assertThrows(
                BookNotFoundException.class,
                () -> bookService.findById(BOOK_ID)
        );

        verify(bookRepository).findById(BOOK_ID);
    }

    @Test
    void createThrowsAndDoesNotSaveWhenPublisherDoesNotExist() {
        BookRequest request = createRequest();
        when(publisherRepository.findById(PUBLISHER_ID))
                .thenReturn(Optional.empty());

        assertThrows(
                PublisherNotFoundException.class,
                () -> bookService.create(request)
        );

        verify(publisherRepository).findById(PUBLISHER_ID);
        verify(bookRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void createReturnsMappedResponseAndSavesWhenPublisherExists() {
        BookRequest request = createRequest();
        PublisherEntity publisher = new PublisherEntity("Addison-Wesley");
        when(publisherRepository.findById(PUBLISHER_ID))
                .thenReturn(Optional.of(publisher));
        when(bookRepository.save(org.mockito.ArgumentMatchers.any(BookEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BookResponse response = bookService.create(request);

        assertNull(response.id());
        assertEquals(request.title(), response.title());
        assertEquals(request.author(), response.author());
        assertEquals(request.copies(), response.copies());
        assertEquals(request.replacementCost(), response.replacementCost());
        assertEquals(request.category(), response.category());
        assertEquals(request.publishedDate(), response.publishedDate());
        assertNotNull(response.createdAt());
        assertTrue(response.available());
        assertNull(response.publisherId());
        assertEquals("Addison-Wesley", response.publisherName());
        verify(publisherRepository).findById(PUBLISHER_ID);
        ArgumentCaptor<BookEntity> savedBook = ArgumentCaptor.forClass(BookEntity.class);
        verify(bookRepository).save(savedBook.capture());
        assertEquals(request.title(), savedBook.getValue().getTitle());
        assertEquals(publisher, savedBook.getValue().getPublisher());
    }

    @Test
    void borrowCopyPropagatesNotAvailableException() {
        BookEntity unavailableBook = createBook(
                0, new PublisherEntity("Addison-Wesley")
        );
        when(bookRepository.findById(BOOK_ID))
                .thenReturn(Optional.of(unavailableBook));

        assertThrows(
                BookNotAvailableException.class,
                () -> bookService.borrowCopy(BOOK_ID)
        );

        verify(bookRepository).findById(BOOK_ID);
    }

    private BookEntity createBook(int copies, PublisherEntity publisher) {
        return new BookEntity(
                "Effective Java",
                "Joshua Bloch",
                copies,
                new BigDecimal("45.00"),
                BookCategory.TECHNOLOGY,
                LocalDate.of(2018, 1, 6),
                publisher
        );
    }

    private BookRequest createRequest() {
        return new BookRequest(
                "Effective Java",
                "Joshua Bloch",
                2,
                new BigDecimal("45.00"),
                BookCategory.TECHNOLOGY,
                LocalDate.of(2018, 1, 6),
                PUBLISHER_ID
        );
    }
}
