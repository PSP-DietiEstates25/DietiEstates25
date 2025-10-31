package com.dietiestates.auth.exception.notfound;

import com.dietiestates.auth.enums.BusinessErrorCodes;
import com.dietiestates.auth.exception.AppException;

public class RoleNotFoundException extends AppException {

    private static final long serialVersionUID = 7508971019979835673L;

    public RoleNotFoundException() {
        super(BusinessErrorCodes.ROLE_NOT_FOUND);
    }
}
