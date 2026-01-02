package com.dietiestates.resource_server.utils;

import com.dietiestates.resource_server.enums.NotificationCategory;
import com.dietiestates.resource_server.exception.notfound.NotificationCategoryNotFoundException;

public class NotificationUtils {

    private NotificationUtils(){}

    public static NotificationCategory extractNotificationCategory(String notificationCategory){
        try {
            return NotificationCategory.valueOf(notificationCategory.toUpperCase());
        } catch (IllegalArgumentException _) {
            throw new NotificationCategoryNotFoundException();
        }
    }

    public static boolean checkNotificationCategoryExists(String notificationCategory){
        return notificationCategory != null && !notificationCategory.isEmpty();
    }
}
