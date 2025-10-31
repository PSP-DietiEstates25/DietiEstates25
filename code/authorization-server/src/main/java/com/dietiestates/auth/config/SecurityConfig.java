package com.dietiestates.auth.config;

import com.dietiestates.auth.federation.FederatedIdentityAuthenticationSuccessHandler;
import com.dietiestates.auth.federation.UserRepositoryOAuth2UserHandler;
import com.dietiestates.auth.repository.JpaRegisteredClientRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

@Configuration
public class SecurityConfig {

    @Value("${loginUrl}")
    private String loginUrl;

    @Value("${registerUrl}")
    private String registerUrl;

    @Value("${loginProcessingUrl}")
    private String loginProcessingUrl;

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(
            HttpSecurity http,
            AuthenticationSuccessHandler userRepositoryOAuth2UserHandler
    ) throws Exception {

        var csrfRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        var csrfHandler = new CsrfTokenRequestAttributeHandler();
        csrfHandler.setCsrfRequestAttributeName(null);

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf
                        .csrfTokenRepository(csrfRepository)
                        .csrfTokenRequestHandler(csrfHandler)
                )
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, registerUrl).permitAll()
                        .requestMatchers(loginProcessingUrl).permitAll()
                        .requestMatchers("/auth/**", "/.well-known/**").permitAll()
                        //federazione Google
                        .requestMatchers("/oauth2/authorization/**", "/login/oauth2/**").permitAll()
                        //swagger
                        .requestMatchers(
                                "/v2/api-docs","/v3/api-docs","/v3/api-docs/**",
                                "/swagger-resources","/swagger-resources/**",
                                "/configuration/ui","/configuration/security",
                                "/swagger-ui/**","/webjars/**","/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage(loginUrl)
                        .loginProcessingUrl(loginProcessingUrl)
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage(loginUrl)
                        .successHandler(userRepositoryOAuth2UserHandler)
                )
                .oauth2Client(Customizer.withDefaults()) // <— necessario per /oauth2/authorization/google
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

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(
            UserRepositoryOAuth2UserHandler userRepositoryOAuth2UserHandler
    ) {

        var federation = new FederatedIdentityAuthenticationSuccessHandler();
        federation.setOAuth2UserHandler(userRepositoryOAuth2UserHandler);
        return federation;
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
