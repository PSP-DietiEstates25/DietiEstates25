package com.authenticationserver.api.exception.alreadyexists;

import com.authenticationserver.api.enums.BusinessErrorCodes;
import com.authenticationserver.api.exception.AppException;

public class AccountAlreadyExistsException extends AppException {

	private static final long serialVersionUID = 1075231391306617908L;

	public AccountAlreadyExistsException() {
		super(BusinessErrorCodes.ACCOUNT_ALREADY_EXISTS);
	}
}
