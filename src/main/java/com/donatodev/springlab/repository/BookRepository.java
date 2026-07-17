package com.donatodev.springlab.repository;

import com.donatodev.springlab.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository Spring Data JPA per BookEntity.
 *
 * Spring Data crea automaticamente
 * l'implementazione concreta a runtime.
 */
public interface BookRepository
        extends JpaRepository<BookEntity, Long> {
}