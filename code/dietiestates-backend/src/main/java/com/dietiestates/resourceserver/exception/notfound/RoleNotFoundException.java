package com.dietiestates.resourceserver.exception.notfound;

import com.dietiestates.resourceserver.enums.BusinessErrorCodes;
import com.dietiestates.resourceserver.exception.AppException;

public class RoleNotFoundException extends AppException {

	public RoleNotFoundException() {
		super(BusinessErrorCodes.ROLE_NOT_FOUND);
	}
}
