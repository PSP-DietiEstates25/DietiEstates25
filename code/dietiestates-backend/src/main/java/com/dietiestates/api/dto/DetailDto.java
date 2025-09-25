package com.dietiestates.api.dto;

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
	
	@Positive(message = "Geographical position id must be a positive number")
	private Long geographicalPositionId;
	
	@Positive(message = "Utility id must be a positive number")
	private Long utilityId;
	
	@Positive(message = "Cadastral data id must be a positive number")
	private Long cadastralDataId;
}
