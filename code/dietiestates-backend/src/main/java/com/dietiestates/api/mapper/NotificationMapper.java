package com.dietiestates.api.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.NotificationRequest;
import com.dietiestates.api.dto.response.NotificationResponse;
import com.dietiestates.api.model.Notification;
import com.dietiestates.api.model.NotificationCategory;
import com.dietiestates.api.model.User;
import com.dietiestates.api.spec.NotificationSpec;

@Component
public class NotificationMapper {

	public Notification toEntity(
			NotificationRequest request,
			NotificationCategory notificationCategory,
			User user
				) {
		return Notification.builder()
				.createdDate(LocalDateTime.now())
				.message(request.getMessage())
				.notificationCategory(notificationCategory)
				.user(user)
				.build();
	}
	
	public NotificationSpec toSpec(NotificationRequest request) {
		return NotificationSpec.builder()
				.message(request.getMessage())
				.notificationCategoryName(request.getNotificationCategoryName())
				.userEmail(request.getUserEmail())
				.build();
	}
	
	public NotificationResponse fromEntity(Notification notification) {
		return NotificationResponse.builder()
				.id(notification.getId())
				.createdDate(notification.getCreatedDate())
				.lastModifiedDate(notification.getLastModifiedDate())
				.message(notification.getMessage())
				.notificationCategoryId(notification.getNotificationCategory().getId())
				.userEmail(notification.getUser().getEmail())
				.build();
	}
}
