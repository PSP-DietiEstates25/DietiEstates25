package com.dietiestates.api.dto;

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
public class ProposalDto {
	
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
	
	@NotEmpty(message = "Real estate id is mandatory")
	@NotBlank(message = "Real estate id is mandatory")
	@Positive(message = "Real estate id must be a positive number")
	private Long realEstateId;

}
