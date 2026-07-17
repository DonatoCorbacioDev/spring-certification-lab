package com.donatodev.springlab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Rappresenta un libro persistito nel database.
 */
@Entity
@Table(name = "books")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 150)
    private String author;

    @Column(nullable = false)
    private Integer copies;

    @Column(
            name = "replacement_cost",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal replacementCost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BookCategory category;

    @Column(name = "published_date", nullable = false)
    private LocalDate publishedDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Costruttore richiesto da JPA.
     */
    protected BookEntity() {
    }

    /**
     * Costruttore usato dal codice applicativo.
     */
    public BookEntity(
            String title,
            String author,
            Integer copies,
            BigDecimal replacementCost,
            BookCategory category,
            LocalDate publishedDate
    ) {
        this.title = title;
        this.author = author;
        this.copies = copies;
        this.replacementCost = replacementCost;
        this.category = category;
        this.publishedDate = publishedDate;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getCopies() {
        return copies;
    }

    public BigDecimal getReplacementCost() {
        return replacementCost;
    }

    public BookCategory getCategory() {
        return category;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}