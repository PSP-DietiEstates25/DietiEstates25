package com.dietiestates.resource_server.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StafferRequest {

    @Email(message = "Email is not valid")
    @NotEmpty(message = "Email is mandatory")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Email(message = "Email is not valid")
    @NotEmpty(message = "Admin creator email is mandatory")
    @NotBlank(message = "Admin creator email is mandatory")
    private String adminEmail;
}

