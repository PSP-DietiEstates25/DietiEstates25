package com.dietiestates.api.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// Configurazione della sicurezza
@EnableMethodSecurity // per interpretare @PreAuthorize
@Configuration
public class SecurityConfig {

	@Autowired
    private JwtAuthFilter jwtAuthFilter;

    // Configurazione CORS
    @Value("${security.cors.allowed-origin}")
    private String allowedOrigin;

    // Configurazione della catena di filtri
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())                              // Disabilita la protezione CSRF, per consentire le richieste API
                .cors(Customizer.withDefaults())                           // Abilita CORS con impostazioni predefinite, per consentire le richieste da origini diverse
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()       // Consente l'accesso a tutte le richieste di autenticazione
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // Consente l'accesso alle richieste amministrative solo agli utenti con ruolo ADMIN
                        .anyRequest().authenticated())                     // Richiede l'autenticazione per tutte le altre richieste
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Aggiunge il filtro JWT prima del filtro di autenticazione

        return http.build();
    }

    // Bean per l'encoder delle password con BCrypt
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean per il gestore dell'autenticazione
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Bean per la configurazione CORS, definisce le origini e i metodi consentiti
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(allowedOrigin));
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // consente cookie o Authorization header tra domini diversi

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
