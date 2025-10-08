package com.dietiestates.api.mapperimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.NotificationRequest;
import com.dietiestates.api.dto.response.NotificationResponse;
import com.dietiestates.api.mapper.NotificationMapper;
import com.dietiestates.api.model.Notification;
import com.dietiestates.api.spec.NotificationSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationMapperImpl implements NotificationMapper {
	
	@Override
	public NotificationSpec toSpec(NotificationRequest request) {
		return NotificationSpec.builder()
				.message(request.getMessage())
				.notificationCategoryName(request.getNotificationCategoryName())
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
				.userEmail(notification.getUser().getSecurityAccountDecorator().getAccountEmail())
				.build();
	}
}
