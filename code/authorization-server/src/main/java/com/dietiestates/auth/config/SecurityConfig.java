package com.dietiestates.auth.config;

import com.dietiestates.auth.federation.FederatedIdentityAuthenticationSuccessHandler;
import com.dietiestates.auth.federation.UserRepositoryOAuth2UserHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthorizationServerProperties properties;

    @Bean
    @Order(2)
    public SecurityFilterChain accountApiChain(HttpSecurity http) throws Exception {

        http
                .securityMatcher("/account/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.PATCH, "/account/password").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

        return http.build();
    }


    @Bean
    @Order(3)
    public SecurityFilterChain defaultSecurityFilterChain(
            HttpSecurity http,
            AuthenticationSuccessHandler userRepositoryOAuth2UserHandler,
            ObjectMapper objectMapper
    ) throws Exception {

        var csrfRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        var csrfHandler = new CsrfTokenRequestAttributeHandler();
        csrfHandler.setCsrfRequestAttributeName(null);

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(properties.registerUrl(), properties.csrfUrl(), "/auth/logout")
                        .csrfTokenRepository(csrfRepository)
                        .csrfTokenRequestHandler(csrfHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, properties.registerUrl(), "/logout").permitAll()
                        .requestMatchers(properties.loginProcessingUrl()).permitAll()
                        .requestMatchers("/auth/**", "/.well-known/**").permitAll()
                        .requestMatchers("/oauth2/authorization/**", "/login/oauth2/**").permitAll()
                        .requestMatchers(
                                "/v2/api-docs","/v3/api-docs","/v3/api-docs/**",
                                "/swagger-resources","/swagger-resources/**",
                                "/configuration/ui","/configuration/security",
                                "/swagger-ui/**","/webjars/**","/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage(properties.loginUrl())
                        .loginProcessingUrl(properties.loginProcessingUrl())
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(new SpaAuthenticationSuccessHandler(objectMapper))
                        .failureHandler(new SpaAuthenticationFailureHandler(objectMapper, properties.loginUrl()))
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage(properties.loginUrl())
                        .successHandler(userRepositoryOAuth2UserHandler)
                )
                .oauth2Client(Customizer.withDefaults())
                .exceptionHandling(ex -> ex
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint(properties.loginUrl()),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                new MediaTypeRequestMatcher(MediaType.APPLICATION_JSON)
                        )
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(PathPatternRequestMatcher.pathPattern(HttpMethod.GET, "/auth/logout"))
                        .logoutSuccessUrl(properties.postLogoutRedirectUri())
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID", "XSRF-TOKEN")
                );


        return http.build();
    }

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter rolesConv = new JwtGrantedAuthoritiesConverter();
        rolesConv.setAuthorityPrefix("");
        rolesConv.setAuthoritiesClaimName("role");

        JwtGrantedAuthoritiesConverter scopeConv = new JwtGrantedAuthoritiesConverter();
        scopeConv.setAuthorityPrefix("SCOPE_");
        scopeConv.setAuthoritiesClaimName("scope");

        return jwt -> {
            Set<GrantedAuthority> merged = new HashSet<>();
            merged.addAll(rolesConv.convert(jwt));
            merged.addAll(scopeConv.convert(jwt));

            JwtAuthenticationConverter delegate = new JwtAuthenticationConverter();
            delegate.setJwtGrantedAuthoritiesConverter(_ -> merged);
            return delegate.convert(jwt);
        };
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
