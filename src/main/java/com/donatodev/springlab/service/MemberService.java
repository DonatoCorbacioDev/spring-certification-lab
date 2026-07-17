package com.donatodev.springlab.service;


import com.donatodev.springlab.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Gestisce la logica applicativa relativa agli iscritti.
 *
 * Dipende dal contratto {@link NotificationService}, non da una sua
 * implementazione concreta, e riceve le dipendenze via costruttore.
 */
@Service
public class MemberService {

    private final NotificationService notificationService;

    private final MemberRepository memberRepository;

    public MemberService(@Qualifier("smsNotificationService")
                         NotificationService notificationService,
                         MemberRepository memberRepository) {
        this.notificationService = notificationService;
        this.memberRepository = memberRepository;
    }

    /**
     * Coordina il salvataggio di un iscritto e l'invio
     * della notifica di benvenuto.
     */
    public void registerMember(String memberName) {

        memberRepository.save(memberName);

        System.out.println("Iscritto registrato: " + memberName);

        notificationService.send(
                "Benvenuto in biblioteca, " + memberName
        );
    }
}
