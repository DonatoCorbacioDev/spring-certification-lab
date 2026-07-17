package com.donatodev.springlab.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boot")
public class BootInfoController {

    @GetMapping("/hello")
    public String hello() {
        return "Spring Boot is running";
    }

    @GetMapping("/info")
    public String info() {
        return "Spring Boot simplifies Spring application setup";
    }
}
