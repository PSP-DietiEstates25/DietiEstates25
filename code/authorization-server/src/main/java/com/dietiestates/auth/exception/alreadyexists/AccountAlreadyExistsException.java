package com.dietiestates.auth.exception.alreadyexists;

import com.dietiestates.auth.enums.BusinessErrorCodes;
import com.dietiestates.auth.exception.AppException;

public class AccountAlreadyExistsException extends AppException {

    private static final long serialVersionUID = 1075231391306617908L;

    public AccountAlreadyExistsException() {
        super(BusinessErrorCodes.ACCOUNT_ALREADY_EXISTS);
    }
}