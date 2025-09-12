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
public class ServicesDto {

	@NotEmpty(message = "Air conditioning is mandatory")
	@NotBlank(message = "Air conditioning is mandatory")
	private boolean hasAirConditioning;
	
	@NotEmpty(message = "Doorman is mandatory")
	@NotBlank(message = "Doorman is mandatory")
	private boolean hasDoorman;
	
	@NotEmpty(message = "Elevator is mandatory")
	@NotBlank(message = "Elevator is mandatory")
	private boolean hasElevator;
	
	@NotEmpty(message = "Details id is mandatory")
	@NotBlank(message = "Details id is mandatory")
	@Positive(message = "Details id must be a positive number")
	private Long detailsId;
}
