package com.dietiestates.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public record AuthorizationServerProperties(
    Integer authorizationServerBaseHttpPort,
    Integer authorizationServerBaseHttpsPort,
    String authorizationServerBaseUri,
    String loginUrl,
    String registerUrl,
    String csrfUrl,
    String loginProcessingUrl,
    String allowedOrigin,
    String clientId,
    String clientSecret,
    String redirectUriOidc,
    String redirectUri,
    String postLogoutRedirectUri,
    String superAdminEmail,
    String superAdminPassword
){}
