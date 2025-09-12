package com.dietiestates.api.exception.notfound;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class UserNotFoundException extends AppException {

	public UserNotFoundException() {
		super(BusinessErrorCodes.USER_NOT_FOUND);
	}
}
