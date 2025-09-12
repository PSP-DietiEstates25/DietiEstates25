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
public class SearchDto {
	
	@NotEmpty(message = "Category is mandatory")
	@NotBlank(message = "Category is mandatory")
	private String category;
	
	@NotEmpty(message = "Minimum price is mandatory")
	@NotBlank(message = "Minimum price is mandatory")
	@Positive(message = "Minimum price must be a positive number")
	private BigDecimal minimumPrice;
	
	@NotEmpty(message = "Maximum price is mandatory")
	@NotBlank(message = "Maximum price is mandatory")
	@Positive(message = "Maximum price must be a positive number")
	private BigDecimal maximumPrice;

	@NotEmpty(message = "Details id is mandatory")
	@NotBlank(message = "Details id is mandatory")
	@Positive(message = "Details id must be a positive number")
	private Long detailsId;
}
