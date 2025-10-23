package com.dietiestates.resourceserver.exception.notowned;

import com.dietiestates.resourceserver.enums.BusinessErrorCodes;
import com.dietiestates.resourceserver.exception.AppException;

public class NotificationNotOwnedByNotificationCategoryException extends AppException {

	private static final long serialVersionUID = 991513701375037484L;

	public NotificationNotOwnedByNotificationCategoryException() {
		super(BusinessErrorCodes.NOTIFICATION_NOT_OWNED_BY_NOTIFICATION_CATEGORY);
	}
}
