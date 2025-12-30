package com.dietiestates.auth.exception.alreadyexists;

import com.dietiestates.auth.enums.BusinessErrorCodes;
import com.dietiestates.auth.exception.AppException;

public class DefaultAccountAlreadyExistsException extends AppException {

    private static final long serialVersionUID = 1075231391306617908L;

    public DefaultAccountAlreadyExistsException() {
        super(BusinessErrorCodes.DEFAULT_ACCOUNT_ALREADY_EXISTS);
    }
}