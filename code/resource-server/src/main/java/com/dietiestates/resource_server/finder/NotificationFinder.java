package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.NotificationNotFoundException;
import com.dietiestates.resource_server.model.Notification;
import com.dietiestates.resource_server.model.NotificationCategory;
import com.dietiestates.resource_server.model.User;

import java.util.List;

public interface NotificationFinder {

	Notification getNotificationById(Long id)
			throws NotificationNotFoundException;

    List<Notification> getPrincipalNotifications(
            User user,
            NotificationCategory notificationCategory
    );
}
