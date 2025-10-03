package com.dietiestates.api.exception.notfound;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class DetailNotFoundException extends AppException {

	public DetailNotFoundException() {
		super(BusinessErrorCodes.DETAIL_NOT_FOUND);
	}
}
