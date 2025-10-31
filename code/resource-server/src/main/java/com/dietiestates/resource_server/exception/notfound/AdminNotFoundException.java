package com.dietiestates.resource_server.exception.notfound;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class AdminNotFoundException extends AppException {

    public AdminNotFoundException() {
        super(BusinessErrorCodes.ADMIN_NOT_FOUND);
    }
}
