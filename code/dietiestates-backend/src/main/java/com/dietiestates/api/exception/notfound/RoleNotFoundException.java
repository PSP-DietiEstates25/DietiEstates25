package com.dietiestates.api.exception.notfound;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class RoleNotFoundException extends AppException {

	public RoleNotFoundException() {
		super(BusinessErrorCodes.ROLE_NOT_FOUND);
	}
}
