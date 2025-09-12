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
public class NotificationDto {

	@NotEmpty(message = "Message is mandatory")
	@NotBlank(message = "Message is mandatory")
	@Size(min = 1, message = "Message must be at least 1 character long")
	@Size(max = 50, message = "Message must be maximum 50 characters long")
	private String message;
	
	@NotEmpty(message = "Notification category id is mandatory")
	@NotBlank(message = "Notification category id is mandatory")
	@Positive(message = "Notification category id must be a positive number")
	private Long notificationCategoryId;
}
