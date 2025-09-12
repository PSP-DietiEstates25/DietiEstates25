package com.dietiestates.api.exception.notfound;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class RealEstateNotFoundException extends AppException {
	
	public RealEstateNotFoundException() {
		super(BusinessErrorCodes.REAL_ESTATE_NOT_FOUND);
	}
}
