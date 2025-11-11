package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.enums.NotificationCategoryType;
import com.dietiestates.resource_server.exception.notfound.NotificationCategoryNotFoundException;
import com.dietiestates.resource_server.model.NotificationCategory;
import com.dietiestates.resource_server.model.User;

public interface NotificationCategoryFinder {
	NotificationCategory getNotificationCategoryByName(String name) throws NotificationCategoryNotFoundException;
    NotificationCategory getNotificationCategoryByNameAndUser(String name, User user) throws NotificationCategoryNotFoundException;
}
