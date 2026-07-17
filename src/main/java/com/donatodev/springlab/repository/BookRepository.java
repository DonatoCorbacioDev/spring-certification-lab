package com.donatodev.springlab.repository;

import com.donatodev.springlab.entity.BookCategory;
import com.donatodev.springlab.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository
        extends JpaRepository<BookEntity, Long> {

    /**
     * Query derivata:
     * filtra per categoria e applica paginazione.
     */
    Page<BookEntity> findByCategory(
            BookCategory category,
            Pageable pageable
    );

    /**
     * Query derivata su una relazione:
     * filtra usando publisher.id.
     */
    Page<BookEntity> findByPublisher_Id(
            Long publisherId,
            Pageable pageable
    );

    /**
     * Query derivata con ricerca parziale
     * e ordinamento dinamico.
     */
    List<BookEntity> findByAuthorContainingIgnoreCase(
            String text,
            Sort sort
    );

    /**
     * Query JPQL esplicita.
     *
     * BookEntity e title sono nomi Java,
     * non nomi di tabella o colonne SQL.
     */
    @Query("""
            select book
            from BookEntity book
            where lower(book.title)
                like lower(concat('%', :text, '%'))
            """)
    Page<BookEntity> searchByTitle(
            @Param("text") String text,
            Pageable pageable
    );
}