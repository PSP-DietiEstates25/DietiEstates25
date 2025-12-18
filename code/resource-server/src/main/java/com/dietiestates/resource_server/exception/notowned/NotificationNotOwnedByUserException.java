package com.dietiestates.resource_server.exception.notowned;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class NotificationNotOwnedByUserException extends AppException {

    private static final long serialVersionUID = 991513701375037484L;

    public NotificationNotOwnedByUserException() {
        super(BusinessErrorCodes.NOTIFICATION_NOT_OWNED_BY_USER);
    }
}
