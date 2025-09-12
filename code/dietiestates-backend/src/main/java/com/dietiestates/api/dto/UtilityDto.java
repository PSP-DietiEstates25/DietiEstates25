package com.dietiestates.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class UtilityDto {

	@NotEmpty(message = "Air conditioning is mandatory")
	@NotBlank(message = "Air conditioning is mandatory")
	private boolean hasAirConditioning;
	
	@NotEmpty(message = "Doorman is mandatory")
	@NotBlank(message = "Doorman is mandatory")
	private boolean hasDoorman;
	
	@NotEmpty(message = "Elevator is mandatory")
	@NotBlank(message = "Elevator is mandatory")
	private boolean hasElevator;
	
}
