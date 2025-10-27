package com.dietiestates.resource_server.exception.notfound;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class RoleNotFoundException extends AppException {

    public RoleNotFoundException() {
        super(BusinessErrorCodes.ROLE_NOT_FOUND);
    }
}
