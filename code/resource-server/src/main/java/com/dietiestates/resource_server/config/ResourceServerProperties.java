package com.dietiestates.resource_server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record ResourceServerProperties (
    String superAdminEmail,
    String imagesFolderPath
){}
