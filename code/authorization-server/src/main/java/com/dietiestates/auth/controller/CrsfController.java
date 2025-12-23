package com.dietiestates.auth.controller;

import com.dietiestates.auth.config.AuthorizationServerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class CrsfController {

    @GetMapping("/auth/csrf")
    public ResponseEntity<Void> csrf(org.springframework.security.web.csrf.CsrfToken token) {
        return ResponseEntity.ok().build();
    }
}