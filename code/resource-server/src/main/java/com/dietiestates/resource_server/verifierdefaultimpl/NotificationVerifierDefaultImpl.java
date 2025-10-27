package com.dietiestates.resource_server.verifierdefaultimpl;

import com.dietiestates.resource_server.exception.notowned.NotificationNotOwnedByNotificationCategoryException;
import com.dietiestates.resource_server.verifier.NotificationVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationVerifierDefaultImpl implements NotificationVerifier {

	@Override
	public void checkNotificationOwnedByNotificationCategory(
			Long notificationNotificationCategoryId,
			Long notiticationCategoryId)
					throws NotificationNotOwnedByNotificationCategoryException {
		if(!notificationNotificationCategoryId.equals(notiticationCategoryId))
			throw new NotificationNotOwnedByNotificationCategoryException();
	}

}
