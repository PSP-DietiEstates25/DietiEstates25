package com.dietiestates.api.service;

import com.dietiestates.api.dto.request.NotificationCategoryRequest;
import com.dietiestates.api.dto.response.NotificationCategoryResponse;

public interface NotificationCategoryService {

	NotificationCategoryResponse createNotificationCategory(NotificationCategoryRequest request);

	NotificationCategoryResponse getNotificationCategoryByName(String notificationCategoryName);

	NotificationCategoryResponse updateNotificationCategoryIsActive(String notificationCategoryName, Boolean isActive);

}
