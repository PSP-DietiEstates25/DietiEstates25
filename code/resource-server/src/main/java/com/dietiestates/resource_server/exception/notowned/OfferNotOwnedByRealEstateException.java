package com.dietiestates.resource_server.exception.notowned;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class OfferNotOwnedByRealEstateException extends AppException {

    private static final long serialVersionUID = 8002736732369411760L;

    public OfferNotOwnedByRealEstateException() {
        super(BusinessErrorCodes.OFFER_NOT_OWNED_BY_REAL_ESTATE);
    }
}
