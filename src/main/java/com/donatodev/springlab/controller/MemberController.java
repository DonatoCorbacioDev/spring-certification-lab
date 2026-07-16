package com.donatodev.springlab.controller;

import com.donatodev.springlab.service.MemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Gestisce le richieste HTTP relative agli iscritti.
 *
 * @RestController permette a Spring di rilevare la classe
 * tramite component scanning e registrarla come Bean.
 *
 * Questa classe appartiene al layer API:
 * riceve le richieste e delega la logica a MemberService.
 */
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    /**
     * Spring inietta il Bean MemberService
     * attraverso il costruttore
     */
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * Endpoint dimostrativo che registra un iscritto.
     *
     * @GetMapping collega questo metodo a una richiesta HTTP GET.
     * Il controller delega la logica applicativa a MemberService
     * e restituisce una risposta testuale al client.
     */
    @GetMapping("/register-demo")
    public String registerDemo() {
        memberService.registerMember("Donato");
        return "Registrazione demo completata";
    }
}
