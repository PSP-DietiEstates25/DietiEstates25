package com.dietiestates.api.verifierImpl;

import com.dietiestates.api.exception.notowned.NotificationNotOwnedByNotificationCategoryException;
import com.dietiestates.api.verifier.NotificationVerifier;

public class NotificationVerifierImpl implements NotificationVerifier {

	@Override
	public void checkNotificationOwnedByNotificationCategory(
			Long notificationNotificationCategoryId,
			Long notiticationCategoryId)
					throws NotificationNotOwnedByNotificationCategoryException {
		if(!notificationNotificationCategoryId.equals(notiticationCategoryId))
			throw new NotificationNotOwnedByNotificationCategoryException();
	}

}
