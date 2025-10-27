package com.dietiestates.resource_server.exception.notfound;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class RealEstateNotFoundException extends AppException {

    public RealEstateNotFoundException() {
        super(BusinessErrorCodes.REAL_ESTATE_NOT_FOUND);
    }
}
