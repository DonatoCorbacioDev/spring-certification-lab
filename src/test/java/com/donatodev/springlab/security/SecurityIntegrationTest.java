package com.donatodev.springlab.security;

import com.donatodev.springlab.entity.BookCategory;
import com.donatodev.springlab.entity.BookEntity;
import com.donatodev.springlab.entity.PublisherEntity;
import com.donatodev.springlab.repository.BookRepository;
import com.donatodev.springlab.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    private Long availableBookId;

    @BeforeEach
    void createAvailableBook() {
        PublisherEntity publisher = publisherRepository.save(
                new PublisherEntity("Security Test Press")
        );
        BookEntity book = bookRepository.save(new BookEntity(
                "Security Testing",
                "Test Author",
                2,
                new BigDecimal("25.00"),
                BookCategory.TECHNOLOGY,
                LocalDate.of(2025, 1, 1),
                publisher
        ));
        availableBookId = book.getId();
    }

    @Test
    void publicEndpointAllowsAnonymousUser() throws Exception {
        mockMvc.perform(get("/api/security/public"))
                .andExpect(status().isOk());
    }

    @Test
    void protectedEndpointRejectsAnonymousUser() throws Exception {
        mockMvc.perform(get("/api/security/me")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userEndpointAllowsUserRole() throws Exception {
        mockMvc.perform(get("/api/security/user"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void adminEndpointRejectsUserRole() throws Exception {
        mockMvc.perform(get("/api/security/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminEndpointAllowsAdminRole() throws Exception {
        mockMvc.perform(get("/api/security/admin"))
                .andExpect(status().isOk());
    }

    @Test
    void actuatorHealthAllowsAnonymousUser() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void actuatorInfoRejectsUserRole() throws Exception {
        mockMvc.perform(get("/actuator/info"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void actuatorInfoAllowsAdminRole() throws Exception {
        mockMvc.perform(get("/actuator/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.app.name").value("Spring Certification Lab"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void borrowWithoutCsrfIsForbidden() throws Exception {
        mockMvc.perform(patch("/api/books/{id}/borrow", availableBookId))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void borrowWithCsrfReachesApplicationAndUpdatesBook() throws Exception {
        mockMvc.perform(patch("/api/books/{id}/borrow", availableBookId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(availableBookId))
                .andExpect(jsonPath("$.copies").value(1));
    }
}
