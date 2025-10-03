package com.dietiestates.api.exception.notowned;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class UtilityNotOwnedByDetailException extends AppException {

	public UtilityNotOwnedByDetailException() {
		super(BusinessErrorCodes.UTILITY_NOT_OWNED_BY_DETAIL);
	}
}
