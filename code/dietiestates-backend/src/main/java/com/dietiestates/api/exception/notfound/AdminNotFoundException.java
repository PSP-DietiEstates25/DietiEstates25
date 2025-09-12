package com.dietiestates.api.exception.notfound;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class AdminNotFoundException extends AppException {

	public AdminNotFoundException() {
		super(BusinessErrorCodes.ADMIN_NOT_FOUND);
	}
}
