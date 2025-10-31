package com.dietiestates.resource_server.exception.notfound;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class DetailNotFoundException extends AppException {

    public DetailNotFoundException() {
        super(BusinessErrorCodes.DETAIL_NOT_FOUND);
    }
}
