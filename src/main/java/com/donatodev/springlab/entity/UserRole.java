package com.donatodev.springlab.entity;

/**
 * Ruoli applicativi persistiti senza il prefisso {@code ROLE_}.
 *
 * <p>Durante la conversione in {@code UserDetails}, il builder
 * {@code roles(...)} aggiunge il prefisso richiesto dalle convenzioni
 * di Spring Security, producendo {@code ROLE_USER} o {@code ROLE_ADMIN}.</p>
 */
public enum UserRole {
    USER,
    ADMIN
}
