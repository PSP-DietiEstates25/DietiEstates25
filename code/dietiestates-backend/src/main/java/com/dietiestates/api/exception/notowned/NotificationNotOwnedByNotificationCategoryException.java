package com.dietiestates.api.exception.notowned;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class NotificationNotOwnedByNotificationCategoryException extends AppException {

	private static final long serialVersionUID = 991513701375037484L;

	public NotificationNotOwnedByNotificationCategoryException() {
		super(BusinessErrorCodes.NOTIFICATION_NOT_OWNED_BY_NOTIFICATION_CATEGORY);
	}
}
