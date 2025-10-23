package com.dietiestates.resourceserver.factory;

import com.dietiestates.resourceserver.model.NotificationCategory;
import com.dietiestates.resourceserver.spec.NotificationCategorySpec;

public interface NotificationCategoryFactory {

	NotificationCategory createNotificationCategoryFromSpec(
			NotificationCategorySpec spec
			);
}
