package com.dietiestates.resourceserver.finder;

import com.dietiestates.resourceserver.exception.notfound.NotificationNotFoundException;
import com.dietiestates.resourceserver.model.Notification;

public interface NotificationFinder {

	Notification getNotificationById(Long id)
			throws NotificationNotFoundException;
}
