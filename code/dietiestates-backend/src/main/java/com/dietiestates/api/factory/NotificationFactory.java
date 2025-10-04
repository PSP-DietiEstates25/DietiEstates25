package com.dietiestates.api.factory;

import com.dietiestates.api.dto.request.NotificationRequest;
import com.dietiestates.api.model.Notification;

public interface NotificationFactory {
	
	Notification createNotification(NotificationRequest request);

}
