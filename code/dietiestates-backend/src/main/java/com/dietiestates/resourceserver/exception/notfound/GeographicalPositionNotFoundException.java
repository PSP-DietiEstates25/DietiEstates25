package com.dietiestates.resourceserver.exception.notfound;

import com.dietiestates.resourceserver.enums.BusinessErrorCodes;
import com.dietiestates.resourceserver.exception.AppException;

public class GeographicalPositionNotFoundException extends AppException {

	public GeographicalPositionNotFoundException() {
		super(BusinessErrorCodes.GEOGRAPHICAL_POSITION_NOT_FOUND);
	}
}
