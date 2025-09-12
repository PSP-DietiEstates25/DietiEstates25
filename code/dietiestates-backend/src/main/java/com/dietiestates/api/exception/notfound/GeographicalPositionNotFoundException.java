package com.dietiestates.api.exception.notfound;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class GeographicalPositionNotFoundException extends AppException {

	public GeographicalPositionNotFoundException() {
		super(BusinessErrorCodes.GEOGRAPHICAL_POSITION_NOT_FOUND);
	}
}
