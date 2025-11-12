package com.dietiestates.resource_server.mapper;

import com.dietiestates.resource_server.dto.request.NotificationCategoryRequest;
import com.dietiestates.resource_server.dto.response.NotificationCategoryResponse;
import com.dietiestates.resource_server.model.NotificationCategory;
import com.dietiestates.resource_server.spec.NotificationCategorySpec;

import java.util.List;

public interface NotificationCategoryMapper {
	NotificationCategorySpec toSpec(NotificationCategoryRequest request);
	NotificationCategoryResponse fromEntity(NotificationCategory notificationCategory);

    List<NotificationCategoryResponse> createNotificationCategoriesResponse(List<NotificationCategory> notificationCategories);
}
