package com.dietiestates.api.mapperimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.NotificationCategoryRequest;
import com.dietiestates.api.dto.response.NotificationCategoryResponse;
import com.dietiestates.api.mapper.NotificationCategoryMapper;
import com.dietiestates.api.model.NotificationCategory;
import com.dietiestates.api.spec.NotificationCategorySpec;

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
