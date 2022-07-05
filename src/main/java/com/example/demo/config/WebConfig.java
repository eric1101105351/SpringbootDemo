package com.example.demo.config;

import com.example.demo.interceptor.TraceIdInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final TraceIdInterceptor traceIdInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(traceIdInterceptor).addPathPatterns("/**").order(Ordered.HIGHEST_PRECEDENCE);
    }

}
