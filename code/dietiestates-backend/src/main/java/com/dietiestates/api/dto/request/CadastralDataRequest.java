package com.dietiestates.api.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
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
public class CadastralDataRequest {

	@NotNull(message = "Price is mandatory")
	@Positive(message = "Price must be a positive number")
	private BigDecimal price;

	@NotNull(message = "Square meters is mandatory")
	@Positive(message = "Square meters must be a positive number")
	private Integer squareMeters;

	@NotBlank(message = "Energy class is mandatory")
	private String energyClass;

	@NotNull(message = "Rooms number is mandatory")
	@Positive(message = "Rooms must be a positive number")
	private Integer rooms;

	@NotNull(message = "Floor is mandatory")
	@Positive(message = "Floor must be a positive number")
	private Integer floor;
}
