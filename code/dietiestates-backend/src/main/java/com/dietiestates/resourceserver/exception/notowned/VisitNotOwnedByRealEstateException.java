package com.dietiestates.resourceserver.exception.notowned;

import com.dietiestates.resourceserver.enums.BusinessErrorCodes;
import com.dietiestates.resourceserver.exception.AppException;

public class VisitNotOwnedByRealEstateException extends AppException {

	private static final long serialVersionUID = 6129523064940762086L;

	public VisitNotOwnedByRealEstateException() {
		super(BusinessErrorCodes.VISIT_NOT_OWNED_BY_REAL_ESTATE);
	}
}
