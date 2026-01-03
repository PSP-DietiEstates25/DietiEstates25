package com.dietiestates.auth.config;

import com.dietiestates.auth.enums.BusinessErrorCodes;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import java.util.Map;

public class SpaAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;
    private final AuthenticationFailureHandler htmlDelegate;

    public SpaAuthenticationFailureHandler(
            ObjectMapper objectMapper,
            String loginUrl
    ){
        this.objectMapper = objectMapper;
        this.htmlDelegate = new SimpleUrlAuthenticationFailureHandler(loginUrl + "?error=");
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {

        if (expectsJson(request)) {
            BusinessErrorCodes businessErrorCode = mapBusinessCode(exception);

            response.setStatus(businessErrorCode.getHttpStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            objectMapper.writeValue(response.getWriter(), Map.of(
                    "businessErrorCode", businessErrorCode.getCode(),
                    "businessErrorMessage", writeAccountAuthenticationFailureMessage(businessErrorCode)
            ));

            return;
        }

        htmlDelegate.onAuthenticationFailure(request, response, exception);
    }

    private boolean expectsJson(HttpServletRequest request) {
        String accept = request.getHeader(HttpHeaders.ACCEPT);
        String xhr = request.getHeader("X-Requested-With");
        return (accept != null && accept.contains(MediaType.APPLICATION_JSON_VALUE))
                || "XMLHttpRequest".equalsIgnoreCase(xhr);
    }

    private BusinessErrorCodes mapBusinessCode(AuthenticationException ex) {
        if (ex instanceof BadCredentialsException) return BusinessErrorCodes.BAD_CREDENTIALS;
        return BusinessErrorCodes.DEFAULT_ACCOUNT_BAD_REQUEST;
    }

    private String writeAccountAuthenticationFailureMessage(BusinessErrorCodes code) {

        return switch (code) {
            case BAD_CREDENTIALS -> "Email and / or password is incorrect";
            default -> "Authentication failed";
        };
    }
}
