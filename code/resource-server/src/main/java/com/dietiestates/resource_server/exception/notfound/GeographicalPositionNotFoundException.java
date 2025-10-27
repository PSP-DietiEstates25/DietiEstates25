package com.dietiestates.resource_server.exception.notfound;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class GeographicalPositionNotFoundException extends AppException {

    public GeographicalPositionNotFoundException() {
        super(BusinessErrorCodes.GEOGRAPHICAL_POSITION_NOT_FOUND);
    }
}
