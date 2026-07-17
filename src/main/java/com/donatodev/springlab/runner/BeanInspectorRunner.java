package com.donatodev.springlab.runner;

import com.donatodev.springlab.service.MemberService;
import com.donatodev.springlab.service.NotificationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

/**
 * Permette di osservare alcuni Bean registrati
 * nell'ApplicationContext.
 *
 * L'ApplicationContext è il container principale di Spring:
 * crea, configura, conserva e collega i Bean.
 */
@Component
public class BeanInspectorRunner implements CommandLineRunner {

    /*
     * Spring inietta il proprio ApplicationContext
     * tramite Constructor Injection.
     */
    private final ApplicationContext applicationContext;

    public BeanInspectorRunner(
            ApplicationContext applicationContext
    ) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(String... args) {

        /*
         * Recupero i nomi di tutti i Bean
         * registrati nell'ApplicationContext.
         */
        String[] beanNames =
                applicationContext.getBeanDefinitionNames();

        System.out.println("\n=== BEAN DEL LABORATORIO ===");

        /*
         * Mostro soltanto alcuni Bean
         * appartenenti al nostro progetto.
         */
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

        /*
         * Recupero due volte MemberService
         * direttamente dal container Spring.
         */
        MemberService firstMemberService =
                applicationContext.getBean(MemberService.class);

        MemberService secondMemberService =
                applicationContext.getBean(MemberService.class);

        /*
         * Verifico se Spring restituisce
         * la stessa istanza singleton.
         */
        System.out.println(
                "MemberService è la stessa istanza? "
                        + (firstMemberService == secondMemberService)
        );

        /*
         * Recupero tutti i Bean che implementano
         * l'interfaccia NotificationService.
         */
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