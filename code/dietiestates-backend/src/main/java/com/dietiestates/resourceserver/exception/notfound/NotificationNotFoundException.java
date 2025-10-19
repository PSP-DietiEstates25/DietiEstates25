package com.dietiestates.resourceserver.exception.notfound;

import com.dietiestates.resourceserver.enums.BusinessErrorCodes;
import com.dietiestates.resourceserver.exception.AppException;

public class NotificationNotFoundException extends AppException {

	public NotificationNotFoundException() {
		super(BusinessErrorCodes.NOTIFICATION_NOT_FOUND);
	}
}
