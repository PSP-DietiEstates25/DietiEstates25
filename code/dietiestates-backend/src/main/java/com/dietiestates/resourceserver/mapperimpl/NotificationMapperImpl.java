package com.dietiestates.resourceserver.mapperimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.dto.request.NotificationRequest;
import com.dietiestates.resourceserver.dto.response.NotificationResponse;
import com.dietiestates.resourceserver.mapper.NotificationMapper;
import com.dietiestates.resourceserver.model.Notification;
import com.dietiestates.resourceserver.spec.NotificationSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationMapperImpl implements NotificationMapper {
	
	@Override
	public NotificationSpec toSpec(NotificationRequest request) {
		return NotificationSpec.builder()
				.message(request.getMessage())
				//.notificationCategoryName(request.getNotificationCategoryName())
				.userEmail(request.getUserEmail())
				.build();
	}
	
	@Override
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
