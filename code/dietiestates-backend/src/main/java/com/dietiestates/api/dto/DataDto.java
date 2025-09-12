package com.dietiestates.api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
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
public class DataDto {
	
	@NotEmpty(message = "Price is mandatory")
	@NotBlank(message = "Price is mandatory")
	@Positive(message = "Price must be a positive number")
	private BigDecimal price;
	
	@NotEmpty(message = "Size is mandatory")
	@NotBlank(message = "Size is mandatory")
	@Digits(fraction = 2, integer = 3)
	@Positive(message = "Size must be a positive number")
	private Float size;

	@NotEmpty(message = "Energy class is mandatory")
	@NotBlank(message = "Energy class is mandatory")
	private String energyClass;
	
	@NotEmpty(message = "Rooms number is mandatory")
	@NotBlank(message = "Rooms number is mandatory")
	@Positive(message = "Rooms must be a positive number")
	private Integer rooms;
	
	@NotEmpty(message = "Floor is mandatory")
	@NotBlank(message = "Floor is mandatory")
	@Positive(message = "Floor must be a positive number")
	private Integer floor;
	
	@NotEmpty(message = "Detail id is mandatory")
	@NotBlank(message = "Detail id is mandatory")
	@Positive(message = "Detail id must be a positive number")
	private Long detailsId;
}
