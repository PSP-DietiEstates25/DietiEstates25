package com.dietiestates.resource_server.spec;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UtilitySpec {

	private Boolean hasAirConditioning;
	private Boolean hasDoorman;
	private Boolean hasElevator;
}
