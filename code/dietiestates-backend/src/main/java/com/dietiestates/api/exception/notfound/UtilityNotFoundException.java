package com.dietiestates.api.exception.notfound;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class UtilityNotFoundException extends AppException {

	public UtilityNotFoundException() {
		super(BusinessErrorCodes.SERVICES_NOT_FOUND);
	}
}
