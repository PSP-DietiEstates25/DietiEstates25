package com.dietiestates.resourceserver.verifier;

import com.dietiestates.resourceserver.exception.notowned.NotificationNotOwnedByNotificationCategoryException;

public interface NotificationVerifier {

	void checkNotificationOwnedByNotificationCategory(
			Long notificationNotificationCategoryId,
			Long notiticationCategoryId
			)
		throws NotificationNotOwnedByNotificationCategoryException;
}
