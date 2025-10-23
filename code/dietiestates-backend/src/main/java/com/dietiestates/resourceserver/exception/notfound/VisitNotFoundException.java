package com.dietiestates.resourceserver.exception.notfound;

import com.dietiestates.resourceserver.enums.BusinessErrorCodes;
import com.dietiestates.resourceserver.exception.AppException;

public class VisitNotFoundException extends AppException {
	
	public VisitNotFoundException() {
		super(BusinessErrorCodes.VISIT_NOT_FOUND);
	}
}
