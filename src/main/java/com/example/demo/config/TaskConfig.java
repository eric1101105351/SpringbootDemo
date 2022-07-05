package com.example.demo.config;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class TaskConfig {

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor() {

            @Override
            public <T> Future<T> submit(Callable<T> task) {
                return super.submit(wrap(task, MDC.getCopyOfContextMap()));
            }

            @Override
            public void execute(Runnable task) {
                super.execute(wrap(task, MDC.getCopyOfContextMap()));
            }
        };
        executor.setAllowCoreThreadTimeOut(true);
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(40);
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(60);
        executor.setAwaitTerminationSeconds(10);
        executor.setBeanName("taskExecutor");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }


    private <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    private Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }

    private void setTraceIdIfAbsent() {
        String correlationId = MDC.get("correlationId");
        if (!StringUtils.hasLength(correlationId)) {
            correlationId = UUID.randomUUID().toString();
        }
        MDC.put("correlationId", correlationId);
    }

}
