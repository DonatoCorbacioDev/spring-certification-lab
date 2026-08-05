package com.donatodev.springlab.exception;

public class BookNotAvailableException extends RuntimeException {

    public BookNotAvailableException(String title) {
        super("Nessuna copia disponibile per il libro: " + title);
    }
}
