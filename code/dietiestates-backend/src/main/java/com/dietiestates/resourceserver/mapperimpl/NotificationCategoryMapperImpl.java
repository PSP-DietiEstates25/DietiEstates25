package com.dietiestates.resourceserver.mapperimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.dto.request.NotificationCategoryRequest;
import com.dietiestates.resourceserver.dto.response.NotificationCategoryResponse;
import com.dietiestates.resourceserver.mapper.NotificationCategoryMapper;
import com.dietiestates.resourceserver.model.NotificationCategory;
import com.dietiestates.resourceserver.spec.NotificationCategorySpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationCategoryMapperImpl implements NotificationCategoryMapper {
	
	@Override
	public NotificationCategorySpec toSpec(NotificationCategoryRequest request) {
		return NotificationCategorySpec.builder()
				.name(request.getName())
				.isActive(request.getIsActive())
				.build();
	}
	
	@Override
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
