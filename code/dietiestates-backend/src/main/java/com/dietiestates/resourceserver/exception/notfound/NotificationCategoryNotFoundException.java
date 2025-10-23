package com.dietiestates.resourceserver.exception.notfound;

import com.dietiestates.resourceserver.enums.BusinessErrorCodes;
import com.dietiestates.resourceserver.exception.AppException;

public class NotificationCategoryNotFoundException extends AppException {

	public NotificationCategoryNotFoundException() {
		super(BusinessErrorCodes.NOTIFICATION_CATEGORY_NOT_FOUND);
	}
}
