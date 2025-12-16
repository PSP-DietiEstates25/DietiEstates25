package com.dietiestates.resource_server.exception.notowned;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class SearchNotOwnedByUserException extends AppException {
    public SearchNotOwnedByUserException(){
        super(BusinessErrorCodes.SEARCH_NOT_OWNED_BY_USER);
    }
}
