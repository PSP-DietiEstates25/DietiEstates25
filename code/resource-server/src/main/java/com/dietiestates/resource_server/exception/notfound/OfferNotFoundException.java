package com.dietiestates.resource_server.exception.notfound;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class OfferNotFoundException extends AppException {

    public OfferNotFoundException() {
        super(BusinessErrorCodes.OFFER_NOT_FOUND);
    }
}
