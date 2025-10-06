package com.dietiestates.api.verifier;

import com.dietiestates.api.exception.notowned.NotificationNotOwnedByNotificationCategoryException;

public interface NotificationVerifier {

	void checkNotificationOwnedByNotificationCategory(
			Long notificationNotificationCategoryId,
			Long notiticationCategoryId
			)
		throws NotificationNotOwnedByNotificationCategoryException;
}
