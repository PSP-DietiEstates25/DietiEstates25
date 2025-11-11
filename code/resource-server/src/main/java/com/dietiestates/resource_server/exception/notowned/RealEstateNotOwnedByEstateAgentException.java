package com.dietiestates.resource_server.exception.notowned;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class RealEstateNotOwnedByEstateAgentException extends AppException {

    public RealEstateNotOwnedByEstateAgentException() {
        super(BusinessErrorCodes.REAL_ESTATE_NOT_OWNED_BY_ESTATE_AGENT);
    }
}
