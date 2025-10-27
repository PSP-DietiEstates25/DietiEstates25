package com.dietiestates.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.auth.dto.request.RegisterRequest;
import com.dietiestates.auth.dto.response.RegisterResponse;
import com.dietiestates.auth.service.RegisterService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import javax.management.relation.RoleNotFoundException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody @Valid RegisterRequest request
    ) throws RoleNotFoundException {
        var account = registerService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }
}
