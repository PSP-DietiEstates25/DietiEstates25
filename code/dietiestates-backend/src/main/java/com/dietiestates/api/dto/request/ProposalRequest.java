package com.dietiestates.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ProposalRequest {
	
	@NotEmpty(message = "Category is mandatory")
	@NotBlank(message = "Category is mandatory")
	private String category;
	
	@NotEmpty(message = "Status is mandatory")
	@NotBlank(message = "Status is mandatory")
	private String status;
	
	@NotEmpty(message = "User email is mandatory")
	@NotBlank(message = "User email is mandatory")
	@Email(message = "User email is not valid")
	private String userEmail;
}
