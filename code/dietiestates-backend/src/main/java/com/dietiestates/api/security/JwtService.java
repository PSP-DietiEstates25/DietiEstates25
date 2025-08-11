package com.dietiestates.api.security;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {

    @Value("${security.jwt.secret}")
    private String secret;
    
    // entrambi presi da application.properties
    @Value("${security.jwt.expiration-ms}")
    private long expirationMs;

    public String generate(String subjectEmail, Map<String, Object> claims){
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);

        return Jwts.builder()                               // jwts è una classe che fornisce metodi per analizzare e generare token JWT
                .setClaims(claims)                          // claims è un oggetto che contiene le informazioni aggiuntive del token, ad esempio il ruolo 
                .setSubject(subjectEmail) 
                .setIssuedAt(now)                           // data di emissione del token
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secret) // HS256 è un algoritmo di firma HMAC basato su SHA-256, cioè utilizza una chiave segreta per firmare il token
                .compact();                                 // genera il token JWT
    }

    public Claims parse(String token){
        return Jwts.parser()           
                .setSigningKey(secret) // imposta la chiave segreta per la verifica della firma
                .parseClaimsJws(token) // analizza il token e verifica la firma, jws è JSON Web Signature serve a garantire l'integrità e l'autenticità del token
                .getBody();            // restituisce il corpo del token, che contiene le informazioni del token come i claims
    }
}
