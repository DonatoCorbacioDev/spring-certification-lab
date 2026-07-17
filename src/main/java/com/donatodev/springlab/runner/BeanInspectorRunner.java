package com.donatodev.springlab.runner;

import com.donatodev.springlab.service.MemberService;
import com.donatodev.springlab.service.NotificationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

/**
 * Runner didattico che permette di osservare alcuni bean registrati
 * nell'{@link ApplicationContext}.
 *
 * L'ApplicationContext è il container principale di Spring:
 * crea, configura, conserva e collega i bean dell'applicazione.
 */
@Component
public class BeanInspectorRunner implements CommandLineRunner {

    private final ApplicationContext applicationContext;

    public BeanInspectorRunner(
            ApplicationContext applicationContext
    ) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(String... args) {

        // Recupera i nomi di tutti i bean registrati nel container.
        String[] beanNames =
                applicationContext.getBeanDefinitionNames();

        System.out.println("\n=== BEAN DEL LABORATORIO ===");

        // Limita l'output ai bean rilevanti per questo esercizio.
        Arrays.stream(beanNames)
                .filter(beanName -> {
                    String lowerCaseName = beanName.toLowerCase();

                    return lowerCaseName.startsWith("member")
                            || lowerCaseName.contains("notificationservice")
                            || lowerCaseName.equals("apprunner")
                            || lowerCaseName.equals("beaninspectorrunner");
                })
                .sorted()
                .forEach(System.out::println);

        // Recupera due volte il bean per dimostrarne lo scope singleton.
        MemberService firstMemberService =
                applicationContext.getBean(MemberService.class);

        MemberService secondMemberService =
                applicationContext.getBean(MemberService.class);

        System.out.println(
                "MemberService è la stessa istanza? "
                        + (firstMemberService == secondMemberService)
        );

        // Recupera tutte le strategie di notifica gestite dal container.
        Map<String, NotificationService> notificationServices =
                applicationContext.getBeansOfType(
                        NotificationService.class
                );

        System.out.println("\n=== IMPLEMENTAZIONI DI NotificationService ===");

        notificationServices.forEach(
                (beanName, bean) ->
                        System.out.println(
                                beanName
                                        + " -> "
                                        + bean.getClass().getSimpleName()
                        )
        );
    }
}