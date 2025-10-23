package com.dietiestates.resourceserver.finder;

import com.dietiestates.resourceserver.exception.notfound.NotificationCategoryNotFoundException;
import com.dietiestates.resourceserver.model.NotificationCategory;

public interface NotificationCategoryFinder {

	NotificationCategory getNotificationCategoryByName(String name)
			throws NotificationCategoryNotFoundException;
}
