package com.dietiestates.api.dto;

import java.math.BigDecimal;

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
public class SearchDto {
	
	@NotEmpty(message = "Category is mandatory")
	@NotBlank(message = "Category is mandatory")
	private String category;
	
	@NotEmpty(message = "Size is mandatory")
	@NotBlank(message = "Size is mandatory")
	@Positive(message = "Size must be a positive number")
	private Integer size;
	
	@NotEmpty(message = "Page is mandatory")
	@NotBlank(message = "Page is mandatory")
	@Positive(message = "Page must be a positive number")
	private Integer page;
	
	@NotEmpty(message = "User email is mandatory")
	@NotBlank(message = "User email is mandatory")
	@Email
	private String userEmail;
}
