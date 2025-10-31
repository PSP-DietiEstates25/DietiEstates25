package com.dietiestates.resource_server.spec;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CadastralFilterSpec {

	private BigDecimal minPrice;
	private BigDecimal maxPrice;
	private Integer minSquareMeters;
	private Integer maxSquareMeters;
	private Integer minEnergyClass;
	private Integer maxEnergyClass;
	private Integer minRooms;
	private Integer maxRooms;
	private Integer minFloor;
	private Integer maxFloor;
}
