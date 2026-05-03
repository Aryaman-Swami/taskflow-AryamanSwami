package com.project.taskflow.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest =
                new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse =
                new ContentCachingResponseWrapper(response);

        long start = System.currentTimeMillis();

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - start;

            String requestBody = new String(
                    wrappedRequest.getContentAsByteArray(),
                    wrappedRequest.getCharacterEncoding()
            );
            String responseBody = new String(
                    wrappedResponse.getContentAsByteArray(),
                    wrappedResponse.getCharacterEncoding()
            );

            log.info("[REQUEST]  {} {} | body: {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    requestBody.isBlank() ? "<empty>" : requestBody);

            log.info("[RESPONSE] {} {} | status: {} | {}ms | body: {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration,
                    responseBody.isBlank() ? "<empty>" : responseBody);

            wrappedResponse.copyBodyToResponse();
        }
    }
}
