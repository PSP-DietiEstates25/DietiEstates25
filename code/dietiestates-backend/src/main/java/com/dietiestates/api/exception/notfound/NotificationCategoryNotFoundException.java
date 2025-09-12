package com.dietiestates.api.exception.notfound;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class NotificationCategoryNotFoundException extends AppException {

	public NotificationCategoryNotFoundException() {
		super(BusinessErrorCodes.NOTIFICATION_CATEGORY_NOT_FOUND);
	}
}
