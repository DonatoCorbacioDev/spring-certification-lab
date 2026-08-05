package com.donatodev.springlab.controller;

import com.donatodev.springlab.dto.request.BookRequest;
import com.donatodev.springlab.dto.response.BookResponse;
import com.donatodev.springlab.entity.BookCategory;
import com.donatodev.springlab.exception.BookNotFoundException;
import com.donatodev.springlab.exception.BookNotAvailableException;
import com.donatodev.springlab.exception.GlobalExceptionHandler;
import com.donatodev.springlab.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class BookControllerTest {

    private static final long BOOK_ID = 42L;
    private static final long PUBLISHER_ID = 7L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    @Test
    void findByIdReturnsBookJson() throws Exception {
        when(bookService.findById(BOOK_ID)).thenReturn(createResponse());

        mockMvc.perform(get("/api/books/{id}", BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(BOOK_ID))
                .andExpect(jsonPath("$.title").value("Effective Java"))
                .andExpect(jsonPath("$.author").value("Joshua Bloch"))
                .andExpect(jsonPath("$.publisherName").value("Addison-Wesley"));
    }

    @Test
    void createReturnsCreatedBookJson() throws Exception {
        BookRequest request = createRequest();
        when(bookService.create(request)).thenReturn(createResponse());

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(BOOK_ID))
                .andExpect(jsonPath("$.title").value(request.title()))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void createRejectsInvalidRequestBeforeCallingService() throws Exception {
        BookRequest invalidRequest = new BookRequest(
                " ", "", 0, BigDecimal.ZERO, null, null, null
        );

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookService);
    }

    @Test
    void createRejectsValuesLongerThanEntityColumns() throws Exception {
        BookRequest invalidRequest = new BookRequest(
                "T".repeat(201),
                "A".repeat(151),
                1,
                new BigDecimal("123456789.123"),
                BookCategory.TECHNOLOGY,
                LocalDate.of(2025, 1, 1),
                PUBLISHER_ID
        );

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookService);
    }

    @Test
    void findByIdReturnsNotFoundErrorWhenServiceThrows() throws Exception {
        when(bookService.findById(BOOK_ID))
                .thenThrow(new BookNotFoundException(BOOK_ID));

        mockMvc.perform(get("/api/books/{id}", BOOK_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.path").value("/api/books/42"));
    }

    @Test
    void borrowReturnsConflictWhenNoCopiesAreAvailable() throws Exception {
        when(bookService.borrowCopy(BOOK_ID))
                .thenThrow(new BookNotAvailableException("Effective Java"));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .patch("/api/books/{id}/borrow", BOOK_ID))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.path").value("/api/books/42/borrow"));
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

    private BookResponse createResponse() {
        return new BookResponse(
                BOOK_ID,
                "Effective Java",
                "Joshua Bloch",
                2,
                new BigDecimal("45.00"),
                BookCategory.TECHNOLOGY,
                LocalDate.of(2018, 1, 6),
                LocalDateTime.of(2026, 8, 5, 10, 0),
                true,
                PUBLISHER_ID,
                "Addison-Wesley"
        );
    }
}
