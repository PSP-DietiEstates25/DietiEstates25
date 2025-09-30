package com.dietiestates.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UtilityRequest {

	@NotNull(message = "Air conditioning is mandatory")
	private Boolean hasAirConditioning;
	
	@NotNull(message = "Doorman is mandatory")
	private Boolean hasDoorman;
	
	@NotNull(message = "Elevator is mandatory")
	private Boolean hasElevator;
}
