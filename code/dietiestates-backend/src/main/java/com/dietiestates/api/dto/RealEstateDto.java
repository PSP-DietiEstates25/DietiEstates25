package com.dietiestates.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class RealEstateDto {
	
	@NotEmpty(message = "Category is mandatory")
	@NotBlank(message = "Category is mandatory")
	private String category;
	
	@NotEmpty(message = "Images are mandatory")
	@NotBlank(message = "Images are mandatory")
	private String[] images;
	
	@NotEmpty(message = "Description is mandatory")
	@NotBlank(message = "Description is mandatory")
	@Size(min = 1, message = "Description must be a maximum of 200 characters long")
	@Size(max = 200, message = "Description must be at least 1 character long")
	private String description;

	@NotEmpty(message = "Details id is mandatory")
	@NotBlank(message = "Details id is mandatory")
	@Positive(message = "Details id must be a positive number")
	private Long detailsId;
}
