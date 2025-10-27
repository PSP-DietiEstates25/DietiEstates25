package com.dietiestates.resource_server.exception.notfound;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class CadastralDataNotFoundException extends AppException {

    public CadastralDataNotFoundException() {
        super(BusinessErrorCodes.CADASTRAL_DATA_NOT_FOUND);
    }
}

