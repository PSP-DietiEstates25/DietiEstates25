package com.dietiestates.resourceserver.factory;

import com.dietiestates.resourceserver.model.Notification;
import com.dietiestates.resourceserver.model.NotificationCategory;
import com.dietiestates.resourceserver.model.User;
import com.dietiestates.resourceserver.spec.NotificationSpec;

public interface NotificationFactory {
	
	Notification createNotificationFromSpec(
			NotificationSpec spec,
			NotificationCategory notificationCategory,
			User user
			);

}
