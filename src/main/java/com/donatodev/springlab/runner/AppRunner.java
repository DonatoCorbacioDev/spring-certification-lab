package com.donatodev.springlab.runner;

import com.donatodev.springlab.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Esegue una piccola operazione di prova
 * dopo l'avvio dell'applicazione Spring Boot.
 *
 * @Component permette a Spring di rilevare questa classe
 * e registrarla come Bean nell'ApplicationContext.
 */
@Component
public class AppRunner implements CommandLineRunner {

    /**
     * AppRunner dipende da MemberService.
     * La dipendenza viene fornita da Spring
     * tramite Constructor Injection.
     */
    private final MemberService memberService;

    public AppRunner(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * Spring Boot esegue automaticamente questo metodo
     * dopo aver creato e collegato tutti i Bean.
     */
    @Override
    public void run(String... args) {
        memberService.registerMember("Donato");
    }
}
