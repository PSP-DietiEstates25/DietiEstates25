package com.dietiestates.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dietiestates.auth.dto.request.RegisterRequest;
import com.dietiestates.auth.dto.response.AccountResponse;
import com.dietiestates.auth.service.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class RegisterController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AccountResponse> register(
            @RequestBody @Valid RegisterRequest request
    ) throws RoleNotFoundException{
        var account = authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }
}
