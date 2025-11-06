package com.dietiestates.resource_server.factory;

import com.dietiestates.resource_server.model.Notification;
import com.dietiestates.resource_server.model.NotificationCategory;
import com.dietiestates.resource_server.model.User;
import com.dietiestates.resource_server.spec.NotificationSpec;

public interface NotificationFactory {

    Notification createNotificationFromSpec(
            NotificationSpec spec,
            NotificationCategory notificationCategory
    );

}
