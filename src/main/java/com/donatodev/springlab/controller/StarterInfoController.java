package com.donatodev.springlab.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Espone informazioni dimostrative
 * sugli Spring Boot starter.
 */
@RestController
@RequestMapping("/api/starters")
public class StarterInfoController {

    @GetMapping("/info")
    public String starterInfo() {
        return "Spring Boot starters group common dependencies for specific features";
    }

    @GetMapping("/web")
    public String webMvcStarterInfo() {
        return "starter-webmvc helps build web applications and REST APIs";
    }

    @GetMapping("/actuator")
    public String actuatorStarterInfo() {
        return "starter-actuator provides monitoring and management endpoints";
    }
}
