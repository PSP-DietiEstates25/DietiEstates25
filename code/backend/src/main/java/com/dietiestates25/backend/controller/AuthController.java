package com.dietiestates25.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.dietiestates25.backend.dto.AuthRequest;
import com.dietiestates25.backend.dto.RegisterRequest;
import com.dietiestates25.backend.dto.AuthResponse;
import com.dietiestates25.backend.Service.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")

public class AuthController {

    @Autowired private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }
}