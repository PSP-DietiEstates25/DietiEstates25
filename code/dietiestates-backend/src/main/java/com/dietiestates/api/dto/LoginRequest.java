package com.dietiestates.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @Email @NotBlank String email, // Email dell'utente, deve essere valida e non vuota
    @NotBlank String password
) {}
