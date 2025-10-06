package com.dietiestates.api.verifierImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.exception.notowned.NotificationNotOwnedByNotificationCategoryException;
import com.dietiestates.api.verifier.NotificationVerifier;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
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
