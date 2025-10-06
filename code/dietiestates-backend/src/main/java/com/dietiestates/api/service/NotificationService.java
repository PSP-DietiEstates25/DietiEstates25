package com.dietiestates.api.service;

import com.dietiestates.api.dto.request.NotificationRequest;
import com.dietiestates.api.dto.response.NotificationResponse;

public interface NotificationService {

	void createNotification(NotificationRequest request);
	
	NotificationResponse getNotificationById(Long id);
}
