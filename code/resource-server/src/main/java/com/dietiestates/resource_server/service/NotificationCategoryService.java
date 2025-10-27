package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.NotificationCategoryRequest;
import com.dietiestates.resource_server.dto.response.NotificationCategoryResponse;

public interface NotificationCategoryService {

	NotificationCategoryResponse createNotificationCategory(NotificationCategoryRequest request);
	
	NotificationCategoryResponse getNotificationCategoryByName(String notificationCategoryName);
}
