package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.NotificationCategoryRequest;
import com.dietiestates.resource_server.dto.request.UpdateNotificationCategoryStatusRequest;
import com.dietiestates.resource_server.dto.response.NotificationCategoryResponse;

import java.util.List;

public interface NotificationCategoryService {
	NotificationCategoryResponse createNotificationCategory(NotificationCategoryRequest request);
	NotificationCategoryResponse getNotificationCategoryByName(String notificationCategoryName);
    List<NotificationCategoryResponse> getUserNotificationCategories(String userEmail);
    void updateNotificationCategory(String notificationCategoryName, UpdateNotificationCategoryStatusRequest request);
}
