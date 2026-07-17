package com.donatodev.springlab.runner;

import com.donatodev.springlab.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Esegue una registrazione dimostrativa
 * dopo l'avvio dell'applicazione Spring Boot.
 */
@Component
public class AppRunner implements CommandLineRunner {

    private final MemberService memberService;

    public AppRunner(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void run(String... args) {
        memberService.registerMember("Donato");
    }
}
