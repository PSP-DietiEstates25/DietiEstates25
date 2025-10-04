package com.dietiestates.api.factory;

import com.dietiestates.api.dto.request.NotificationCategoryRequest;
import com.dietiestates.api.model.NotificationCategory;

public interface NotificationCategoryFactory {

	NotificationCategory createNotificationCategory(NotificationCategoryRequest request);
}
