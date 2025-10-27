package com.dietiestates.resource_server.exception.notfound;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class NotificationNotFoundException extends AppException {

    public NotificationNotFoundException() {
        super(BusinessErrorCodes.NOTIFICATION_NOT_FOUND);
    }
}
