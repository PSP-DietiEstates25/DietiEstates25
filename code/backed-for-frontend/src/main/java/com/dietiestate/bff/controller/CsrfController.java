package com.dietiestate.bff.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsrfController {
    @GetMapping("/csrf-token")
    public ResponseEntity<Void> csrf(CsrfToken token) {
        // solo accedendo al CsrfToken lo forzi a essere generato & serializzato nel cookie
        return ResponseEntity.noContent().build();
    }
}
