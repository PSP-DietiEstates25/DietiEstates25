package com.dietiestates.api.finder;

import com.dietiestates.api.exception.notfound.NotificationNotFoundException;
import com.dietiestates.api.model.Notification;

public interface NotificationFinder {

	Notification getNotificationById(Long id)
			throws NotificationNotFoundException;
}
