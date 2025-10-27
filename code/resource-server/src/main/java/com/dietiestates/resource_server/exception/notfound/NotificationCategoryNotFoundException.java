package com.dietiestates.resource_server.exception.notfound;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class NotificationCategoryNotFoundException extends AppException {

    public NotificationCategoryNotFoundException() {
        super(BusinessErrorCodes.NOTIFICATION_CATEGORY_NOT_FOUND);
    }
}
