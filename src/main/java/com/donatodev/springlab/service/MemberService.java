package com.donatodev.springlab.service;


import org.springframework.stereotype.Service;

/**
 * Gestisce la logica applicativa relativi agli iscritti.
 *
 * Questa classe non crea direttamente EmailNotificationService:
 * dipende dall'interfaccia NotificationService e riceve
 * l'implementazione concreta tramite Constructor Injection.
 */
@Service
public class MemberService {

    /**
     * la dipendenza è final perché MemberService ne ha bisogno
     * per funzionare e non deve ssere sostituita dopo la creazione
     */
    private final NotificationService notificationService;

    /**
     * Spring usa questo costruttore per iniettare il Bean
     * che implementa NotificationService.
     */
    public MemberService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Simula la registrazione di un nuovo iscritto
     * e richiede l'invio di una notifica di benvenuto.
     */
    public void registerMember(String memberName) {
        System.out.println("Iscritto registrato: " + memberName);

        notificationService.send(
                "Benvenuto in biblioteca, " + memberName
        );
    }
}
