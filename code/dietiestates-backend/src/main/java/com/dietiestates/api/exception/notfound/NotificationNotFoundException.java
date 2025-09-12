package com.dietiestates.api.exception.notfound;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class NotificationNotFoundException extends AppException {

	public NotificationNotFoundException() {
		super(BusinessErrorCodes.NOTIFICATION_NOT_FOUND);
	}
}
