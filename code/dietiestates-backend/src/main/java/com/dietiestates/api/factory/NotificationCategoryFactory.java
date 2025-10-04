package com.dietiestates.api.factory;

import com.dietiestates.api.model.NotificationCategory;
import com.dietiestates.api.spec.NotificationCategorySpec;

public interface NotificationCategoryFactory {

	NotificationCategory createNotificationCategoryFromSpec(
			NotificationCategorySpec spec
			);
}
