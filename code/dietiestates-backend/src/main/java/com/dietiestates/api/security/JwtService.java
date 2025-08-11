package com.dietiestates.api.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
  @Value("${security.jwt.secret}")
  private String secret;

  @Value("${security.jwt.expiration-ms}")
  private long expirationMs;

  private SecretKey key() {
    return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String generate(String subjectEmail, Map<String, Object> claims){
    Date now = new Date();
    Date exp = new Date(now.getTime() + expirationMs);

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subjectEmail)
        .setIssuedAt(now)
        .setExpiration(exp)
        .signWith(key(), SignatureAlgorithm.HS256)
        .compact();
  }

  public Claims parse(String token){
    return Jwts.parserBuilder()
        .setSigningKey(key())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}

