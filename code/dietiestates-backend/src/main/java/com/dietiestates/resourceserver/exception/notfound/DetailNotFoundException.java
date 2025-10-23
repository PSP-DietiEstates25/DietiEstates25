package com.dietiestates.resourceserver.exception.notfound;

import com.dietiestates.resourceserver.enums.BusinessErrorCodes;
import com.dietiestates.resourceserver.exception.AppException;

public class DetailNotFoundException extends AppException {

	public DetailNotFoundException() {
		super(BusinessErrorCodes.DETAIL_NOT_FOUND);
	}
}
