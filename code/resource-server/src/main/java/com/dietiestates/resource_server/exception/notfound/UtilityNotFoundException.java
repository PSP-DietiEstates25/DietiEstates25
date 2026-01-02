package com.dietiestates.resource_server.exception.notfound;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class UtilityNotFoundException extends AppException {

    public UtilityNotFoundException() {
        super(BusinessErrorCodes.UTILITY_NOT_FOUND);
    }
}