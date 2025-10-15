package com.authenticationserver.api.exception.notfound;

import com.authenticationserver.api.enums.BusinessErrorCodes;
import com.authenticationserver.api.exception.AppException;

public class RoleNotFoundException extends AppException {

	private static final long serialVersionUID = 7508971019979835673L;

	public RoleNotFoundException() {
		super(BusinessErrorCodes.ROLE_NOT_FOUND);
	}
}
