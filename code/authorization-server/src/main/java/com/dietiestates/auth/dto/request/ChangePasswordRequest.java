package com.dietiestates.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {

    @NotEmpty(message = "Old password is mandatory")
    @NotBlank(message = "Old password is mandatory")
    private String oldPassword;

    @NotEmpty(message = "New password is mandatory")
    @NotBlank(message = "New password is mandatory")
    @Size(min = 5, max = 64, message = "New password length must be between 5 and 64 characters")
    private String newPassword;
}
