package com.dietiestates.resource_server.exception.notowned;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class UtilityNotOwnedByDetailException extends AppException {

    private static final long serialVersionUID = -6402703313078004476L;

    public UtilityNotOwnedByDetailException() {
        super(BusinessErrorCodes.UTILITY_NOT_OWNED_BY_DETAIL);
    }
}
