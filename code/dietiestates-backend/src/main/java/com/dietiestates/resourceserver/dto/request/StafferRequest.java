package com.dietiestates.resourceserver.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StafferRequest extends AuthenticationRequest {

	@Email
	@NotEmpty(message = "Email is mandatory")
	@NotBlank(message = "Email is mandatory")
	private String adminEmail;
	
	@Builder(builderMethodName = "stafferDtoBuilder")
	public StafferRequest(
			String email,
			String password,
			String adminEmail
			) {
		super(email, password);
		this.adminEmail = adminEmail;
	}
}
