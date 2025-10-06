package com.dietiestates.api.exception.notowned;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class VisitNotOwnedByRealEstateException extends AppException {

	public VisitNotOwnedByRealEstateException() {
		super(BusinessErrorCodes.VISIT_NOT_OWNED_BY_REAL_ESTATE);
	}
}
