package com.dietiestates.authorization.exception.notfound;

import com.dietiestates.authorization.enums.BusinessErrorCodes;
import com.dietiestates.authorization.exception.AppException;

public class RoleNotFoundException extends AppException {

	private static final long serialVersionUID = 7508971019979835673L;

	public RoleNotFoundException() {
		super(BusinessErrorCodes.ROLE_NOT_FOUND);
	}
}
