package com.dietiestates.resource_server.verifier;

import com.dietiestates.resource_server.exception.notfound.NotificationNotFoundException;
import com.dietiestates.resource_server.exception.notowned.NotificationNotOwnedByNotificationCategoryException;

public interface NotificationVerifier {
    void checkNotificationExists(Long id) throws NotificationNotFoundException;
    void checkNotificationOwnedByNotificationCategory(Long id, String notificationCategoryName) throws NotificationNotOwnedByNotificationCategoryException;
}
