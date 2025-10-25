package com.dietiestates.auth.config;

import com.dietiestates.auth.federation.FederatedIdentityAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
public class SecurityConfig {

    @Value("${loginUrl}")
    private String loginUrl;

    @Value("${registerUrl}")
    private String registerUrl;

    @Value("${loginProcessingUrl}")
    private String loginProcessingUrl;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.ignoringRequestMatchers(
                        (RequestMatcher) req -> "POST".equalsIgnoreCase(req.getMethod()) && req.getRequestURI().endsWith(registerUrl),
                        (RequestMatcher) req -> "POST".equalsIgnoreCase(req.getMethod()) && req.getRequestURI().endsWith(loginProcessingUrl)
                ))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, registerUrl).permitAll()
                        .requestMatchers(loginProcessingUrl).permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/.well-known/**").permitAll()
                        .requestMatchers(
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
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                            .loginPage(loginUrl)
                            .loginProcessingUrl(loginProcessingUrl)
                            .permitAll()
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .loginPage(loginUrl)
                                //.successHandler(authenticationSuccessHandler())
                );
                /*
                .exceptionHandling(ex -> ex
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint(loginUrl),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                new MediaTypeRequestMatcher(MediaType.APPLICATION_JSON)
                        )
                );
                */

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new FederatedIdentityAuthenticationSuccessHandler();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
