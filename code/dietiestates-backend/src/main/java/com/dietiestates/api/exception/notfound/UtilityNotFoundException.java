package com.dietiestates.api.exception.notfound;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

import lombok.Builder;

public class UtilityNotFoundException extends AppException {

	public UtilityNotFoundException() {
		super(BusinessErrorCodes.UTILITY_NOT_FOUND);
	}
}
