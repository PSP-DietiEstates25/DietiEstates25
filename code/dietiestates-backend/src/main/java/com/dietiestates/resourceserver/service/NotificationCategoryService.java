package com.dietiestates.resourceserver.service;

import com.dietiestates.resourceserver.dto.request.NotificationCategoryRequest;
import com.dietiestates.resourceserver.dto.response.NotificationCategoryResponse;

public interface NotificationCategoryService {

	NotificationCategoryResponse createNotificationCategory(NotificationCategoryRequest request);
	
	NotificationCategoryResponse getNotificationCategoryByName(String notificationCategoryName);
}
