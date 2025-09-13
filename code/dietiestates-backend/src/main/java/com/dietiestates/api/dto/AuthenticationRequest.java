package com.dietiestates.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthenticationRequest {

	@Email(message = "Email is not valid")
	@NotEmpty(message = "Email is mandatory")
	@NotBlank(message = "Email is mandatory")
	private String email;
	
	@Size(min = 5, message = "Password should be at least 5 characters long")
	@Size(max = 15, message = "Password should be maximum 15 characters long")
	@NotEmpty(message = "Password is mandatory")
	@NotBlank(message = "Password is mandatory")
	private String password;
	
	@NotEmpty(message = "Role is mandatory")
	@NotBlank(message = "Role is mandatory")
	private String role;
}
