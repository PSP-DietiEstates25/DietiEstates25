package com.dietiestates.resource_server.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;



import lombok.RequiredArgsConstructor;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors(Customizer.withDefaults());
        http.csrf(csrf -> csrf.ignoringRequestMatchers(
                "/users",
                "/users/**",
                "/v1/swagger-ui/**",
                "/v1/v3/api-docs/**",
                "/v1/openapi.json"
        ));
        http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
        );
        http.authorizeHttpRequests(
                req -> req
                        .requestMatchers(
                                "/users",
                                "/users/**",
                                "/v1/swagger-ui/**",
                                "/v1/swagger-ui.html",
                                "/v1/v3/api-docs/**",
                                "/v1/openapi.json",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/openapi.json"
                        ).permitAll()
                        .requestMatchers(
                                "/images/**",
                                "/estateagents",
                                "/estateagents/**",
                                "/admins",
                                "/admins/**",
                                "/realestates",
                                "/realestates/**",
                                "/notificationcategories",
                                "/notificationcategories/**",
                                "/searches",
                                "/searches/**",
                                "/offers",
                                "/offers/**",
                                "/visits",
                                "/visits/**",
                                "/cadastraldata",
                                "/cadastraldata/**",
                                "/cadastralfilters",
                                "/cadastralfilters/**",
                                "/details",
                                "/details/**",
                                "/geographicalpositions",
                                "/geographicalpositions/**",
                                "/utilities",
                                "/utilities/**",
                                "/notifications",
                                "/notifications/**"
                        ).authenticated()
                        .anyRequest().denyAll()
        );
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {

        //convertitore per claim "roles" new jwt
        JwtGrantedAuthoritiesConverter rolesConv = new JwtGrantedAuthoritiesConverter();
        rolesConv.setAuthorityPrefix("");
        rolesConv.setAuthoritiesClaimName("role");

        //convertitore per scopes
        JwtGrantedAuthoritiesConverter scopeConv = new JwtGrantedAuthoritiesConverter();
        scopeConv.setAuthorityPrefix("SCOPE_");
        scopeConv.setAuthoritiesClaimName("scope");

        return jwt -> {
            Set<GrantedAuthority> merged = new HashSet<>();
            merged.addAll(rolesConv.convert(jwt));
            merged.addAll(scopeConv.convert(jwt));

            JwtAuthenticationConverter delegate = new JwtAuthenticationConverter();
            delegate.setJwtGrantedAuthoritiesConverter(__ -> merged);
            return delegate.convert(jwt);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
