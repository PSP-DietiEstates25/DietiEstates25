package com.dietiestates.resource_server.utils;

import com.dietiestates.resource_server.enums.NotificationCategory;
import com.dietiestates.resource_server.exception.notfound.NotificationCategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationUtils {

    public static NotificationCategory extractNotificationCategory(String notificationCategory){
        try {
            return NotificationCategory.valueOf(notificationCategory.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NotificationCategoryNotFoundException();
        }
    }

    public static boolean checkNotificationCategoryExists(String notificationCategory){
        return notificationCategory != null && !notificationCategory.isEmpty();
    }
}
