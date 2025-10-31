package com.dietiestates.resource_server.exception.notfound;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class UserNotFoundException extends AppException {

    public UserNotFoundException() {
        super(BusinessErrorCodes.USER_NOT_FOUND);
    }
}
