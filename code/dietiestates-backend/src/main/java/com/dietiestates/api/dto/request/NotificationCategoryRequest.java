package com.dietiestates.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class NotificationCategoryRequest {

	@NotEmpty(message = "Name is mandatory")
	@NotBlank(message = "Name is mandatory")
	private String name;
	
	@NotNull(message = "isActive is mandatory")
	private Boolean isActive;
}
