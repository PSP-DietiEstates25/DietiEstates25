package com.dietiestates.api.exception.notfound;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class DataNotFoundException extends AppException {

	public DataNotFoundException() {
		super(BusinessErrorCodes.DATA_NOT_FOUND);
	}
}
