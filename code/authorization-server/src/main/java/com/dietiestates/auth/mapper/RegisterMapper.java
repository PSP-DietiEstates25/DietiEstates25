package com.dietiestates.auth.mapper;

import com.dietiestates.auth.dto.response.RegisterResponse;
import com.dietiestates.auth.model.SecurityAccount;
import com.dietiestates.auth.spec.RegisterSpec;
import org.springframework.stereotype.Component;

import com.dietiestates.auth.dto.request.RegisterRequest;

@Component
public class RegisterMapper {

    public RegisterSpec toSpec(RegisterRequest request) {
        return RegisterSpec.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .enabled(true)
                .locked(false)
                .build();
    }

    public RegisterResponse fromEntity(SecurityAccount account) {
        return RegisterResponse.builder()
                .id(account.getDefaultAccount().getId())
                .email(account.getDefaultAccount().getEmail())
                .role(account.getDefaultAccount().getRole().getName().toString())
                .enabled(true)
                .locked(false)
                .build();
    }
}