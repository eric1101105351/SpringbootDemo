package com.example.demo.interceptor;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class TraceIdInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        injectTraceId(request);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        removeTraceId();
    }

    private void injectTraceId(HttpServletRequest request) {
        String correlationId = request.getHeader("correlationId");
        if (!StringUtils.hasLength(correlationId)) {
            correlationId = UUID.randomUUID().toString();
        }
        request.setAttribute("correlationId", correlationId);
        MDC.put("correlationId", correlationId);
    }

    private void removeTraceId() {
        MDC.remove("correlationId");
    }

}
