package com.dietiestates.api.dto;

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
public class DetailsDto {
	
	@Positive(message = "Search id must be a positive number")
	private Long searchId;
	
	@Positive(message = "Real estate id must be a positive number")
	private Long realEstateId;
	
	@NotEmpty(message = "Geographical position id is mandatory")
	@NotBlank(message = "Geographical position id is mandatory")
	@Positive(message = "Geographical position id must be a positive number")
	private Long geographicalPositionId;
	
	@NotEmpty(message = "Services id is mandatory")
	@NotBlank(message = "Services id is mandatory")
	@Positive(message = "Services id must be a positive number")
	private Long servicesId;
	
	@NotEmpty(message = "Data id is mandatory")
	@NotBlank(message = "Data id is mandatory")
	@Positive(message = "Data id must be a positive number")
	private Long dataId;
}
