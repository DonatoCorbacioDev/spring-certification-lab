package com.donatodev.springlab.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Espone endpoint didattici dedicati
 * all'autoconfigurazione di Spring Boot.
 */
@RestController
@RequestMapping("/api/autoconfig")
public class AutoConfigurationController {

    @GetMapping("/info")
    public String autoConfigurationInfo() {
        return "Spring Boot auto-configuration uses classpath, beans and properties";
    }

    @GetMapping("/web")
    public String webAutoConfigurationInfo() {
        return "starter-webmvc allows Spring Boot to configure a web application";
    }
}