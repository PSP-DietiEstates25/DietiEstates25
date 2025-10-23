package com.dietiestates.resourceserver.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class NotificationResponse {

	private Long id;
	private LocalDateTime createdDate;
	private LocalDateTime lastModifiedDate;
	private String message;
	private Long notificationCategoryId;
	private String userEmail;
}
