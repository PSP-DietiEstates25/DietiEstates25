package com.dietiestates.api.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.NotificationCategoryRequest;
import com.dietiestates.api.dto.response.NotificationCategoryResponse;
import com.dietiestates.api.enums.NotificationCategoryType;
import com.dietiestates.api.model.NotificationCategory;

@Component
public class NotificationCategoryMapper {

	public NotificationCategory toEntity(NotificationCategoryRequest request) {
		return NotificationCategory.builder()
				.createdDate(LocalDateTime.now())
				.name(NotificationCategoryType.valueOf(request.getName()))
				.isActive(request.getIsActive())
				.build();
	}
	
	public NotificationCategoryResponse fromEntity(NotificationCategory notificationCategory) {
		return NotificationCategoryResponse.builder()
				.id(notificationCategory.getId())
				.createdDate(notificationCategory.getCreatedDate())
				.lastModifiedDate(notificationCategory.getLastModifiedDate())
				.name(notificationCategory.getName().toString())
				.isActive(notificationCategory.getIsActive())
				.build();
	}
}
