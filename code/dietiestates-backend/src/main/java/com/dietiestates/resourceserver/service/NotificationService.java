package com.dietiestates.resourceserver.service;

import com.dietiestates.resourceserver.dto.request.NotificationRequest;
import com.dietiestates.resourceserver.dto.response.NotificationResponse;
import com.dietiestates.resourceserver.exception.notowned.NotificationNotOwnedByNotificationCategoryException;

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
