package com.dietiestates.resource_server.exception.notowned;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class RealEstateNotOwnedByAdminException extends AppException {

    public RealEstateNotOwnedByAdminException(){
        super(BusinessErrorCodes.REAL_ESTATE_NOT_OWNED_BY_ADMIN);
    }
}
