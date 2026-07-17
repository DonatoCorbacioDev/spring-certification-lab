package com.donatodev.springlab.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Mostra le configurazioni caricate
 * dal profilo Spring attivo.
 */
@RestController
@RequestMapping("/api/profiles")
public class ProfileInfoController {

    private final String environment;
    private final String message;
    private final String owner;

    public ProfileInfoController(
            @Value("${app.environment}") String environment,
            @Value("${app.message}") String message,
            @Value("${app.owner}") String owner
    ) {
        this.environment = environment;
        this.message = message;
        this.owner = owner;
    }

    @GetMapping("/environment")
    public String environment() {
        return "Active environment: " + environment;
    }

    @GetMapping("/message")
    public String message() {
        return message;
    }

    @GetMapping("/info")
    public String info() {
        return "Environment: " + environment
                + " | Message: " + message
                + " | Owner: " + owner;
    }
}