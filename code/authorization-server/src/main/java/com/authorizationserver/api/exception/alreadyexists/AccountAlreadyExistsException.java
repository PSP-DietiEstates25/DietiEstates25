package com.authorizationserver.api.exception.alreadyexists;

import com.authorizationserver.api.enums.BusinessErrorCodes;
import com.authorizationserver.api.exception.AppException;

public class AccountAlreadyExistsException extends AppException {

	private static final long serialVersionUID = 1075231391306617908L;

	public AccountAlreadyExistsException() {
		super(BusinessErrorCodes.ACCOUNT_ALREADY_EXISTS);
	}
}
