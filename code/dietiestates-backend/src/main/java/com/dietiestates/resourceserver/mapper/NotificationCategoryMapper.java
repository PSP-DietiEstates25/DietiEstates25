package com.dietiestates.resourceserver.mapper;

import com.dietiestates.resourceserver.dto.request.NotificationCategoryRequest;
import com.dietiestates.resourceserver.dto.response.NotificationCategoryResponse;
import com.dietiestates.resourceserver.model.NotificationCategory;
import com.dietiestates.resourceserver.spec.NotificationCategorySpec;

public interface NotificationCategoryMapper {

	NotificationCategorySpec toSpec(NotificationCategoryRequest request);
	
	NotificationCategoryResponse fromEntity(NotificationCategory notificationCategory);
}
