package com.dietiestates.resourceserver.dto.request;

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
public class CadastralDataRequest {
	
	@NotEmpty(message = "Price is mandatory")
	@NotBlank(message = "Price is mandatory")
	@Positive(message = "Price must be a positive number")
	private BigDecimal price;
	
	@NotEmpty(message = "Size is mandatory")
	@NotBlank(message = "Size is mandatory")
	@Positive(message = "Size must be a positive number")
	private Integer squareMeters;

	@NotEmpty(message = "Energy class is mandatory")
	@NotBlank(message = "Energy class is mandatory")
	@Positive(message = "Size must be a positive number")
	private String energyClass;
	
	@NotEmpty(message = "Rooms number is mandatory")
	@NotBlank(message = "Rooms number is mandatory")
	@Positive(message = "Rooms must be a positive number")
	private Integer rooms;
	
	@NotEmpty(message = "Floor is mandatory")
	@NotBlank(message = "Floor is mandatory")
	@Positive(message = "Floor must be a positive number")
	private Integer floor;
}
