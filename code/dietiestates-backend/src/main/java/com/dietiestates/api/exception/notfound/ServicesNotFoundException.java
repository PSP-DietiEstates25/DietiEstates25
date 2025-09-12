package com.dietiestates.api.exception.notfound;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class ServicesNotFoundException extends AppException {

	public ServicesNotFoundException() {
		super(BusinessErrorCodes.SERVICES_NOT_FOUND);
	}
}
