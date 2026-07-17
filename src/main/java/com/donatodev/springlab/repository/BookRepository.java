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
     * Query derivata dal nome del metodo: filtra per categoria
     * e applica la paginazione ricevuta.
     */
    Page<BookEntity> findByCategory(
            BookCategory category,
            Pageable pageable
    );

    /**
     * Query derivata che attraversa la relazione {@code publisher}
     * e filtra in base al suo identificativo.
     */
    Page<BookEntity> findByPublisher_Id(
            Long publisherId,
            Pageable pageable
    );

    /**
     * Query derivata per una ricerca parziale, senza distinzione
     * tra maiuscole e minuscole, con ordinamento dinamico.
     */
    List<BookEntity> findByAuthorContainingIgnoreCase(
            String text,
            Sort sort
    );

    /**
     * Query JPQL esplicita per una ricerca parziale sul titolo.
     *
     * JPQL usa il nome dell'entity e dei suoi attributi Java,
     * non i nomi della tabella e delle colonne SQL.
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