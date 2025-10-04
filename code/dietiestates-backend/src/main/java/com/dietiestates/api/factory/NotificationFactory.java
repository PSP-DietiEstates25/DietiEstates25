package com.dietiestates.api.factory;

import com.dietiestates.api.model.Notification;
import com.dietiestates.api.model.NotificationCategory;
import com.dietiestates.api.model.User;
import com.dietiestates.api.spec.NotificationSpec;

public interface NotificationFactory {
	
	Notification createNotificationFromSpec(
			NotificationSpec spec,
			NotificationCategory notificationCategory,
			User user
			);

}
