package com.dietiestates.resource_server.exception.notfound;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class VisitNotFoundException extends AppException {

    public VisitNotFoundException() {
        super(BusinessErrorCodes.VISIT_NOT_FOUND);
    }
}
