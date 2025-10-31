package com.dietiestates.resource_server.exception.notfound;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class CadastralFilterNotFoundException extends AppException {

    public CadastralFilterNotFoundException() {
        super(BusinessErrorCodes.CADASTRAL_FILTER_NOT_FOUND);
    }
}
