package com.donatodev.springlab.entity;

import com.donatodev.springlab.exception.BookNotAvailableException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity JPA che rappresenta un libro persistito nel database.
 *
 * Il libro è il lato proprietario della relazione molti-a-uno con l'editore:
 * la colonna {@code publisher_id} contiene la chiave esterna. Il fetch LAZY
 * rimanda il caricamento dell'editore finché la relazione non viene utilizzata.
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "publisher_id", nullable = false)
    private PublisherEntity publisher;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Costruttore senza argomenti richiesto da JPA.
     */
    protected BookEntity() {
    }

    public BookEntity(
            String title,
            String author,
            Integer copies,
            BigDecimal replacementCost,
            BookCategory category,
            LocalDate publishedDate,
            PublisherEntity publisher
    ) {
        this.title = title;
        this.author = author;
        this.copies = copies;
        this.replacementCost = replacementCost;
        this.category = category;
        this.publishedDate = publishedDate;
        this.createdAt = LocalDateTime.now();
        this.publisher = publisher;
    }

    /**
     * Registra il prestito di una copia applicando la regola di dominio
     * che impedisce di prestare un libro non disponibile.
     */
    public void borrowCopy() {
        if (copies == null || copies <= 0) {
            throw new BookNotAvailableException(title);
        }

        copies--;
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

    public PublisherEntity getPublisher() {
        return publisher;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
