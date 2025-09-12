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
public class DetailsDto {
	
	@Positive(message = "Search id must be a positive number")
	private Long searchId;
	
	@Positive(message = "Real estate id must be a positive number")
	private Long realEstateId;
	
}
