package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.NotificationCategoryNotFoundException;
import com.dietiestates.resource_server.model.NotificationCategory;

public interface NotificationCategoryFinder {

	NotificationCategory getNotificationCategoryByName(String name)
			throws NotificationCategoryNotFoundException;
}
