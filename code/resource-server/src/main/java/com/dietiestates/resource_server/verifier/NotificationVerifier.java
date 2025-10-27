package com.dietiestates.resource_server.verifier;

import com.dietiestates.resource_server.exception.notowned.NotificationNotOwnedByNotificationCategoryException;

public interface NotificationVerifier {

	void checkNotificationOwnedByNotificationCategory(
			Long notificationNotificationCategoryId,
			Long notiticationCategoryId
			)
		throws NotificationNotOwnedByNotificationCategoryException;
}
