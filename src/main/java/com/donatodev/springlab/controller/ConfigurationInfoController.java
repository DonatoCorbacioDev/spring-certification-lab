package com.donatodev.springlab.controller;

import com.donatodev.springlab.config.AppProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Espone alcuni valori letti dalla configurazione esterna.
 */
@RestController
@RequestMapping("/api/config")
public class ConfigurationInfoController {

    private final AppProperties appProperties;

    public ConfigurationInfoController(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @GetMapping("/message")
    public String message() {
        return appProperties.message();
    }

    @GetMapping("/owner")
    public String owner() {
        return "Project owner: " + appProperties.owner();
    }

    @GetMapping("/info")
    public String info() {
        return "Spring Boot reads configuration from application.yml";
    }
}
