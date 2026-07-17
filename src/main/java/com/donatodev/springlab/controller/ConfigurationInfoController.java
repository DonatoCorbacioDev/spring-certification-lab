package com.donatodev.springlab.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Espone alcuni valori letti dalla configurazione esterna.
 */
@RestController
@RequestMapping("/api/config")
public class ConfigurationInfoController {

    private final String appMessage;
    private final String appOwner;

    public ConfigurationInfoController(
            @Value("${app.message}") String appMessage,
            @Value("${app.owner}") String appOwner
    ) {
        this.appMessage = appMessage;
        this.appOwner = appOwner;
    }

    @GetMapping("/message")
    public String message() {
        return appMessage;
    }

    @GetMapping("/owner")
    public String owner() {
        return "Project owner: " + appOwner;
    }

    @GetMapping("/info")
    public String info() {
        return "Spring Boot reads configuration from application.yml";
    }
}