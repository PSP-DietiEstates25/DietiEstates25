package com.dietiestates.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class NotificationCategoryDto {

	@NotEmpty(message = "Name is mandatory")
	@NotBlank(message = "Name is mandatory")
	private String name;
	
	@NotEmpty(message = "isActive is mandatory")
	@NotBlank(message = "isActive is mandatory")
	private Boolean isActive;
}
