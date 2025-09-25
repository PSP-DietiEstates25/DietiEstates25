package com.dietiestates.api.dto;

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
public class DetailDto {

	@NotEmpty(message = "Real estate id is mandatory")
	@NotBlank(message = "Real estate id is mandatory")
	@Positive(message = "Real estate id must be a positive number")
	private Long realEstateId;
	
	@NotEmpty(message = "Search id is mandatory")
	@NotBlank(message = "Search id is mandatory")
	@Positive(message = "Search id must be a positive number")
	private Long searchId;
}
