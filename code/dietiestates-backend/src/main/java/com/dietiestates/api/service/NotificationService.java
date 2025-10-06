package com.dietiestates.api.service;

import com.dietiestates.api.dto.request.NotificationRequest;
import com.dietiestates.api.dto.response.NotificationResponse;
import com.dietiestates.api.exception.notowned.NotificationNotOwnedByNotificationCategoryException;

public interface NotificationService {

	void createNotification(NotificationRequest request);
	
	NotificationResponse getNotificationById(
			String notificationCategoryName,
			Long notificationId
			)
		throws NotificationNotOwnedByNotificationCategoryException;
}
