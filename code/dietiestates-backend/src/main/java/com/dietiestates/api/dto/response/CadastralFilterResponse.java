package com.dietiestates.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class CadastralFilterResponse {

	private Long id;
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
	private LocalDateTime createdDate;
	private LocalDateTime lastModifiedDate;
	private Long searchId;
}
