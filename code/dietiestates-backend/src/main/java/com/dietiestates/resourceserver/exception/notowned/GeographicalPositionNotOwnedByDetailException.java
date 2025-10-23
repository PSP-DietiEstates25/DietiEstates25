package com.dietiestates.resourceserver.exception.notowned;

import com.dietiestates.resourceserver.enums.BusinessErrorCodes;
import com.dietiestates.resourceserver.exception.AppException;

public class GeographicalPositionNotOwnedByDetailException extends AppException {

	private static final long serialVersionUID = -1270129960503817628L;

	public GeographicalPositionNotOwnedByDetailException() {
		super(BusinessErrorCodes.GEOGRAPHICAL_POSITION_NOT_OWNED_BY_DETAIL);
	}
}
