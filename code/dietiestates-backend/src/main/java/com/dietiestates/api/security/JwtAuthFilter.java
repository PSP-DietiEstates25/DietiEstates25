package com.dietiestates.api.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

	
// OncePerRequestFilter assicura che il filtro venga applicato una sola volta per ogni richiesta
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
    private JwtService jwtService;

    // doFilterInternal viene chiamato per ogni richiesta, serve per gestire l'autenticazione
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {   // Controlla se l'intestazione è presente e inizia con "Bearer " (tipo di token utilizzato)
            filterChain.doFilter(request, response);                    
            return;
        }

        String token = header.substring(7); // index 7 perchè rimuoviamo "Bearer " (7 caratteri)
        try {
            Claims claims = jwtService.parse(token);   // Analizza il token e ottiene i claims
            String email = claims.getSubject();
            String role = (String) claims.get("role"); // user, admin, agent

            var auth = new UsernamePasswordAuthenticationToken(email, null,
                    role != null ? Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())) // Crea l'oggetto di autenticazione con i ruoli
                            : Collections.emptyList());

            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception exception) {
            // token invalido
        }

        filterChain.doFilter(request, response); // doFilter viene chiamato per continuare la catena di filtri
    }
}
