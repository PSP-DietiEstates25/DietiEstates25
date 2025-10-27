package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.NotificationNotFoundException;
import com.dietiestates.resource_server.model.Notification;

public interface NotificationFinder {

	Notification getNotificationById(Long id)
			throws NotificationNotFoundException;
}
