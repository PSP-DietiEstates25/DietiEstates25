package com.dietiestates.resourceserver.exception.notfound;

import com.dietiestates.resourceserver.enums.BusinessErrorCodes;
import com.dietiestates.resourceserver.exception.AppException;

public class RealEstateNotFoundException extends AppException {
	
	public RealEstateNotFoundException() {
		super(BusinessErrorCodes.REAL_ESTATE_NOT_FOUND);
	}
}
