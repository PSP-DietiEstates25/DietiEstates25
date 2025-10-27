package com.dietiestates.resource_server.exception.notowned;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class NotificationNotOwnedByNotificationCategoryException extends AppException {

    private static final long serialVersionUID = 991513701375037484L;

    public NotificationNotOwnedByNotificationCategoryException() {
        super(BusinessErrorCodes.NOTIFICATION_NOT_OWNED_BY_NOTIFICATION_CATEGORY);
    }
}
