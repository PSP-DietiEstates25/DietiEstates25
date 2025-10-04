package com.dietiestates.api.mapper;

import com.dietiestates.api.dto.request.NotificationCategoryRequest;
import com.dietiestates.api.dto.response.NotificationCategoryResponse;
import com.dietiestates.api.model.NotificationCategory;
import com.dietiestates.api.spec.NotificationCategorySpec;

public interface NotificationCategoryMapper {

	NotificationCategorySpec toSpec(NotificationCategoryRequest request);
	
	NotificationCategoryResponse fromEntity(NotificationCategory notificationCategory);
}
