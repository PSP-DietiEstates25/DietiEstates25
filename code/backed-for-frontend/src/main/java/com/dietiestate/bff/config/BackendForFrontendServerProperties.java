package com.dietiestate.bff.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record BackendForFrontendServerProperties (
        String baseUri,
        String authorizationServerBaseUrl,
        String resourceServerBaseUrl,
        String backendForFrontendServerBaseUrl,
        String registerUrl,
        String allowedOrigin,
        String logoutUrl,
        String callbackUrl,
        String csrfTokenUrl
){}
