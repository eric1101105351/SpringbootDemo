package com.example.demo.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.example.demo.util.GsonUtils.toJson;

@Aspect
@Component
@Slf4j
public class InvokeLogAspect {

    private static final String INPUT_LOG_PATTERN = "\n[SYSTEM]\n        -> {} {}\n        Input : {}";
    private static final String OUTPUT_LOG_PATTERN = "\n[SYSTEM]\n        -> {} {}\n        Output : {}, Duration : {} ms";

    @Pointcut("execution(* com.example.demo.controller..*.*(..))")
    public void invoke() {
        // 定義切點
    }

    @Around("invoke()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime startTime = LocalDateTime.now();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.info(INPUT_LOG_PATTERN, request.getMethod(), request.getRequestURL(), toJson(joinPoint.getArgs()));

        Object result = null;
        try {
            result = joinPoint.proceed();
        } finally {
            LocalDateTime endTime = LocalDateTime.now();
            long duration = ChronoUnit.MILLIS.between(startTime, endTime);
            log.info(OUTPUT_LOG_PATTERN, request.getMethod(), request.getRequestURL(), toJson(result), duration);
        }
        return result;
    }

}