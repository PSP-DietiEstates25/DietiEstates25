package com.dietiestates.api.finder;

import com.dietiestates.api.exception.notfound.NotificationCategoryNotFoundException;
import com.dietiestates.api.model.NotificationCategory;

public interface NotificationCategoryFinder {

	NotificationCategory getNotificationCategoryByName(String name)
			throws NotificationCategoryNotFoundException;
}
