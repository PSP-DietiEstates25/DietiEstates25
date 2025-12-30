package com.dietiestates.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;
import java.util.Map;

public class SpaAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final AuthenticationSuccessHandler htmlDelegate = new SavedRequestAwareAuthenticationSuccessHandler();
    private final RequestCache requestCache = new HttpSessionRequestCache();

    public SpaAuthenticationSuccessHandler(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        if (expectsJson(request)) {
            SavedRequest savedRequest = requestCache.getRequest(request, response);
            String redirectUrl = (savedRequest != null) ? savedRequest.getRedirectUrl() : "/";

            requestCache.removeRequest(request, response);

            response.setStatus(200);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(), Map.of("redirectUrl", redirectUrl));
            return;
        }

        htmlDelegate.onAuthenticationSuccess(request, response, authentication);
    }

    private boolean expectsJson(HttpServletRequest request) {
        String accept = request.getHeader(HttpHeaders.ACCEPT);
        String xhr = request.getHeader("X-Requested-With");
        return (accept != null && accept.contains(MediaType.APPLICATION_JSON_VALUE))
                || "XMLHttpRequest".equalsIgnoreCase(xhr);
    }
}
