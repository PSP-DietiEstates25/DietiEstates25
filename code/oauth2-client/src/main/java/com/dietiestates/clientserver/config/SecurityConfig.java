package com.dietiestates.clientserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		/* vecchio(versione thymeleaf)
		http.authorizeHttpRequests(
				authorize -> authorize.anyRequest().authenticated()
			);
		*/
		http.authorizeHttpRequests(
				authorize -> authorize.requestMatchers(
						"/",
						"/public"
						)
				.permitAll()
				.anyRequest()
				.authenticated()
				);
		http.oauth2Login(Customizer.withDefaults());
		
		return http.build();
	}
}

