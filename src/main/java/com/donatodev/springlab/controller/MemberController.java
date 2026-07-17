package com.donatodev.springlab.controller;

import com.donatodev.springlab.service.MemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Gestisce le richieste HTTP relative agli iscritti
 * e delega la logica applicativa a {@link MemberService}.
 *
 * Appartiene al layer API e non contiene logica di registrazione.
 */
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * Endpoint didattico che avvia una registrazione dimostrativa.
     */
    @GetMapping("/register-demo")
    public String registerDemo() {
        memberService.registerMember("Donato");
        return "Registrazione demo completata";
    }
}
