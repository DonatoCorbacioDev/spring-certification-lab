package com.donatodev.springlab.repository;

import com.donatodev.springlab.entity.PublisherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository Spring Data JPA
 * dedicato agli editori.
 */
public interface PublisherRepository
        extends JpaRepository<PublisherEntity, Long> {
}