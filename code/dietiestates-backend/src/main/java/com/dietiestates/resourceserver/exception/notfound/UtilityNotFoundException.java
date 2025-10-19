package com.dietiestates.resourceserver.exception.notfound;

import com.dietiestates.resourceserver.enums.BusinessErrorCodes;
import com.dietiestates.resourceserver.exception.AppException;

import lombok.Builder;

public class UtilityNotFoundException extends AppException {

	public UtilityNotFoundException() {
		super(BusinessErrorCodes.UTILITY_NOT_FOUND);
	}
}
