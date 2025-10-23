package com.dietiestates.resourceserver.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class CadastralFilterRequest {

	@NotNull(message = "Minimum price is mandatory")
	@Positive(message = "Minimum price must be a positive number")
	private BigDecimal minPrice;
	
	@NotNull(message = "Maximum price is mandatory")
	@Positive(message = "Maximum price must be a positive number")
	private BigDecimal maxPrice;
	
	@NotNull(message = "Minimum square meters is mandatory")
	@Positive(message = "Minimum square meters must be a positive number")
	private Integer minSquareMeters;
	
	@NotNull(message = "Maximum square meters is mandatory")
	@Positive(message = "Maximum square meters must be a positive number")
	private Integer maxSquareMeters;
	
	@NotNull(message = "Minimum energy class is mandatory")
	@Positive(message = "Minimum energy class must be a positive number")
	private Integer minEnergyClass;
	
	@NotNull(message = "Maximum energy class is mandatory")
	@Positive(message = "Maximum energy class must be a positive number")
	private Integer maxEnergyClass;
	
	@NotNull(message = "Minimum rooms is mandatory")
	@Positive(message = "Minimum rooms must be a positive number")
	private Integer minRooms;
	
	@NotNull(message = "Maximum rooms is mandatory")
	@Positive(message = "Maximum rooms must be a positive number")
	private Integer maxRooms;
	
	@NotNull(message = "Minimum floor is mandatory")
	@Positive(message = "Minimum floor must be a positive number")
	private Integer minFloor;
	
	@NotNull(message = "Maximum floor is mandatory")
	@Positive(message = "Maximum floor must be a positive number")
	private Integer maxFloor;
}
