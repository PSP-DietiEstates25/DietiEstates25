package com.dietiestates.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.dietiestates.api.dto.LoginRequest;
import com.dietiestates.api.dto.LoginResponse;
import com.dietiestates.api.dto.RegisterRequest;
import com.dietiestates.api.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;

    public AuthController(AuthService auth) {
        this.auth = auth;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Validated @RequestBody LoginRequest req) {
        return ResponseEntity.ok(auth.login(req));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Validated @RequestBody RegisterRequest req) {
        auth.register(req);
        return ResponseEntity.ok().build();
    }
}
