package com.dietiestates.resourceserver.verifierImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.exception.notowned.NotificationNotOwnedByNotificationCategoryException;
import com.dietiestates.resourceserver.verifier.NotificationVerifier;

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
