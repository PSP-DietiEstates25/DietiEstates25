package com.dietiestates.resource_server.exception.notfound;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class SearchNotFoundException extends AppException {

    public SearchNotFoundException() {
        super(BusinessErrorCodes.SEARCH_NOT_FOUND);
    }
}