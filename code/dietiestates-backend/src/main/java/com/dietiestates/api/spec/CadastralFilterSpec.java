package com.dietiestates.api.spec;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
