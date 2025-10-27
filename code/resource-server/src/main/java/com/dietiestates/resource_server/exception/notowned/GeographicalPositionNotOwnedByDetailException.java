package com.dietiestates.resource_server.exception.notowned;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class GeographicalPositionNotOwnedByDetailException extends AppException {

    private static final long serialVersionUID = -1270129960503817628L;

    public GeographicalPositionNotOwnedByDetailException() {
        super(BusinessErrorCodes.GEOGRAPHICAL_POSITION_NOT_OWNED_BY_DETAIL);
    }
}
