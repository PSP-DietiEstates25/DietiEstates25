package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.NotificationRequest;
import com.dietiestates.resource_server.dto.response.NotificationResponse;
import com.dietiestates.resource_server.exception.notowned.NotificationNotOwnedByNotificationCategoryException;

public interface NotificationService {

	NotificationResponse createNotification(
			String notificationCategoryName,
			NotificationRequest request
			);
	
	NotificationResponse getNotificationById(
			String notificationCategoryName,
			Long notificationId
			)
		throws NotificationNotOwnedByNotificationCategoryException;
}
