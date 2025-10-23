package com.dietiestates.resourceserver.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.dietiestates.resourceserver.filter.JwtFilter;
import com.dietiestates.resourceserver.filter.RequestAuthHeaderValidationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

	private final RequestAuthHeaderValidationFilter requestValidationFilter;
	private final JwtFilter jwtAuthFilter;
	
	private final AuthenticationProvider authenticationProvider;

	@Value("${security.cors.allowed-origin:http://localhost:4200}")
	private String allowedOrigin;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http.cors(Customizer.withDefaults());
		http.csrf(AbstractHttpConfigurer::disable);
		http.authenticationProvider(authenticationProvider);
		http.authorizeHttpRequests(
				req -> req
				.requestMatchers(
						"/auth/estateagent",
						"/auth/admins",
						"/auth/realestates",
						"/notificationcategories"
						).hasRole("ADMIN")
				.requestMatchers(
						"/realestates",
						"/offers",
						"/visits",
						"/cadastraldata",
						"/details",
						"/geographicalpositions",
						"/utilities"
						).hasRole("ESTATE_AGENT")
				.requestMatchers(
						"/searches",
						"/visits",
						"/offers",
						"/cadastralfilters",
						"/details",
						"/geographicalpositions",
						"/utilities",
						"notifications"
						).hasRole("USER")
				.anyRequest().permitAll());
				/*
				.requestMatchers(
						"/auth/**",
						// "/**"y,
						"/v2/api-docs",
						"/v3/api-docs",
						"/v3/api-docs/**",
						"/swagger-resources",
						"/swagger-resources/**",
						"/configuration/ui",
						"/configuration/security",
						"/swagger-ui/**",
						"/webjars/**",
						"/swagger-ui.html"
				).authenticated().anyRequest().permitAll()
				
				);*/
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(requestValidationFilter, JwtFilter.class);

		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of(allowedOrigin));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setExposedHeaders(List.of("Authorization"));
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source; // OK: implementa CorsConfigurationSource
	}
}
