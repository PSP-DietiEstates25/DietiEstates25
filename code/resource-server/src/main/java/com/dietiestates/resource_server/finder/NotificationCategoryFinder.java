package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.enums.NotificationCategoryType;
import com.dietiestates.resource_server.exception.notfound.NotificationCategoryNotFoundException;
import com.dietiestates.resource_server.model.NotificationCategory;
import com.dietiestates.resource_server.model.User;

import java.util.List;

public interface NotificationCategoryFinder {
	NotificationCategory getNotificationCategoryByName(String name) throws NotificationCategoryNotFoundException;
    NotificationCategory getNotificationCategoryByNameAndUserId(String name, Long userId) throws NotificationCategoryNotFoundException;
    List<NotificationCategory> getUserNotificationCategories(Long userId);
}
