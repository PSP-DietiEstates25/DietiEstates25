package com.dietiestates.auth;

import com.dietiestates.auth.config.AuthorizationServerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(AuthorizationServerProperties.class)
public class AuthorizationServerApplication {
    public static void main(String[] args) {
		SpringApplication.run(AuthorizationServerApplication.class, args);
	}
}
