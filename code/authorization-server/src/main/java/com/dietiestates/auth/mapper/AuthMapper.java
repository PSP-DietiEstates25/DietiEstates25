package com.dietiestates.auth.mapper;

import com.dietiestates.auth.dto.response.AuthResponse;
import com.dietiestates.auth.model.SecurityAccount;
import com.dietiestates.auth.spec.AuthSpec;
import org.springframework.stereotype.Component;

import com.dietiestates.auth.dto.request.AuthRequest;

@Component
public class AuthMapper {

    public AuthSpec toSpec(AuthRequest request) {
        return AuthSpec.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .enabled(true)
                .locked(false)
                .build();
    }

    public AuthResponse fromEntity(SecurityAccount account) {
        return AuthResponse.builder()
                .id(account.getDefaultAccount().getId())
                .email(account.getDefaultAccount().getEmail())
                .role(account.getDefaultAccount().getRole().getName().toString())
                .enabled(true)
                .locked(false)
                .build();
    }
}