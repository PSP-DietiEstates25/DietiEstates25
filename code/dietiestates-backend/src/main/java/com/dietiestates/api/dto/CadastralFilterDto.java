package com.dietiestates.api.dto;

import java.math.BigDecimal;

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
public class CadastralFilterDto {

	@NotEmpty(message = "Minimum price is mandatory")
	@NotBlank(message = "Minimum price is mandatory")
	@Positive(message = "Minimum price must be a positive number")
	private BigDecimal minPrice;
	
	@NotEmpty(message = "Maximum price is mandatory")
	@NotBlank(message = "Maximum price is mandatory")
	@Positive(message = "Maximum price must be a positive number")
	private BigDecimal maxPrice;
	
	@NotEmpty(message = "Minimum square meters is mandatory")
	@NotBlank(message = "Minimum square meters is mandatory")
	@Positive(message = "Minimum square meters must be a positive number")
	private Integer minSquareMeters;
	
	@NotEmpty(message = "Maximum square meters is mandatory")
	@NotBlank(message = "Maximum square meters is mandatory")
	@Positive(message = "Maximum square meters must be a positive number")
	private Integer maxSquareMeters;
	
	@NotEmpty(message = "Minimum energy class is mandatory")
	@NotBlank(message = "Minimum energy class is mandatory")
	@Positive(message = "Minimum energy class must be a positive number")
	private Integer minEnergyClass;
	
	@NotEmpty(message = "Maximum energy class is mandatory")
	@NotBlank(message = "Maximum energy class is mandatory")
	@Positive(message = "Maximum energy class must be a positive number")
	private Integer maxEnergyClass;
	
	@NotEmpty(message = "Minimum rooms is mandatory")
	@NotBlank(message = "Minimum rooms is mandatory")
	@Positive(message = "Minimum rooms must be a positive number")
	private Integer minRooms;
	
	@NotEmpty(message = "Maximum rooms is mandatory")
	@NotBlank(message = "Maximum rooms is mandatory")
	@Positive(message = "Maximum rooms must be a positive number")
	private Integer maxRooms;
	
	@NotEmpty(message = "Minimum floor is mandatory")
	@NotBlank(message = "Minimum floor is mandatory")
	@Positive(message = "Minimum floor must be a positive number")
	private Integer minFloor;
	
	@NotEmpty(message = "Maximum floor is mandatory")
	@NotBlank(message = "Maximum floor is mandatory")
	@Positive(message = "Maximum floor must be a positive number")
	private Integer maxFloor;
}
