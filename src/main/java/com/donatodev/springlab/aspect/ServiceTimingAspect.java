package com.donatodev.springlab.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceTimingAspect {

    private static final Logger log = LoggerFactory.getLogger(ServiceTimingAspect.class);

    @Around("execution(* com.donatodev.springlab.service.BookService.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();

        try {
            return joinPoint.proceed();
        } finally {
            long durationNanos = System.nanoTime() - start;
            log.debug(
                    "Metodo {} completato in {} ns",
                    joinPoint.getSignature().toShortString(),
                    durationNanos
            );
        }
    }
}
