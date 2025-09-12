package com.dietiestates.api.exception.notfound;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class VisitNotFoundException extends AppException {
	
	public VisitNotFoundException() {
		super(BusinessErrorCodes.VISIT_NOT_FOUND);
	}
}
