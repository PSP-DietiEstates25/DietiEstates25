package com.dietiestates.resource_server.factory;

import com.dietiestates.resource_server.model.NotificationCategory;
import com.dietiestates.resource_server.model.User;
import com.dietiestates.resource_server.spec.NotificationCategorySpec;

public interface NotificationCategoryFactory {

    NotificationCategory createNotificationCategoryFromSpec(
            NotificationCategorySpec spec,
            User user
    );
}
