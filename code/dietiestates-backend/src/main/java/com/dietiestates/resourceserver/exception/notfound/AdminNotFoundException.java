package com.dietiestates.resourceserver.exception.notfound;

import com.dietiestates.resourceserver.enums.BusinessErrorCodes;
import com.dietiestates.resourceserver.exception.AppException;

public class AdminNotFoundException extends AppException {

	public AdminNotFoundException() {
		super(BusinessErrorCodes.ADMIN_NOT_FOUND);
	}
}
