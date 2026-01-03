package com.dietiestate.bff.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsrfController {
    @GetMapping({"/csrf-token","/api/csrf-token"})
    public ResponseEntity<Void> csrf(CsrfToken token) {
        return ResponseEntity.noContent().build();
    }
}
