package com.donatodev.springlab.controller;

import com.donatodev.springlab.config.AppProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Espone i valori di configurazione determinati
 * dal profilo Spring attivo.
 */
@RestController
@RequestMapping("/api/profiles")
public class ProfileInfoController {

    private final AppProperties appProperties;

    public ProfileInfoController(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @GetMapping("/environment")
    public String environment() {
        return "Active environment: " + appProperties.environment();
    }

    @GetMapping("/message")
    public String message() {
        return appProperties.message();
    }

    @GetMapping("/info")
    public String info() {
        return "Environment: " + appProperties.environment()
                + " | Message: " + appProperties.message()
                + " | Owner: " + appProperties.owner();
    }
}
