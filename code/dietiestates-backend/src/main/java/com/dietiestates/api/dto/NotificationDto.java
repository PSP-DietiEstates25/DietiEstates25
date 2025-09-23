package com.dietiestates.api.dto;

import jakarta.validation.constraints.Email;
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
	
	@NotEmpty(message = "Notification category name is mandatory")
	@NotBlank(message = "Notification category name is mandatory")
	@Positive(message = "Notification category name must be a positive number")
	private String notificationCategoryName;
	
	@NotEmpty(message = "User email is mandatory")
	@NotBlank(message = "User email id is mandatory")
	@Email
	private String userEmail;
}
