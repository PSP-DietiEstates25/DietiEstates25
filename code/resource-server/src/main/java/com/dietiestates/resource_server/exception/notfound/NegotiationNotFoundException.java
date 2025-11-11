package com.dietiestates.resource_server.exception.notfound;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class NegotiationNotFoundException extends AppException {

    public NegotiationNotFoundException() {
        super(BusinessErrorCodes.NEGOTIATION_NOT_FOUND);
    }
}
