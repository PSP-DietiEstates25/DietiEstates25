package com.dietiestates.auth.exception.notfound;

import com.dietiestates.auth.enums.BusinessErrorCodes;
import com.dietiestates.auth.exception.AppException;

public class DefaultAccountNotFoundException extends AppException {
    public DefaultAccountNotFoundException(){
        super(BusinessErrorCodes.DEFAULT_ACCOUNT_NOT_FOUND);
    }
}
