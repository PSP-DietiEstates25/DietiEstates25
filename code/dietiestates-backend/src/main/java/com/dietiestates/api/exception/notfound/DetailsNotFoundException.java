package com.dietiestates.api.exception.notfound;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class DetailsNotFoundException extends AppException {

	public DetailsNotFoundException() {
		super(BusinessErrorCodes.DETAILS_NOT_FOUND);
	}
}
