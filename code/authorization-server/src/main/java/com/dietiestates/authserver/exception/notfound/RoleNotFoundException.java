package com.dietiestates.authserver.exception.notfound;

import com.dietiestates.authserver.enums.BusinessErrorCodes;
import com.dietiestates.authserver.exception.AppException;

public class RoleNotFoundException extends AppException {

	private static final long serialVersionUID = 7508971019979835673L;

	public RoleNotFoundException() {
		super(BusinessErrorCodes.ROLE_NOT_FOUND);
	}
}
