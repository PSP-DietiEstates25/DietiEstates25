package com.dietiestates.authserver.exception.alreadyexists;

import com.dietiestates.authserver.enums.BusinessErrorCodes;
import com.dietiestates.authserver.exception.AppException;

public class AccountAlreadyExistsException extends AppException {

	private static final long serialVersionUID = 1075231391306617908L;

	public AccountAlreadyExistsException() {
		super(BusinessErrorCodes.ACCOUNT_ALREADY_EXISTS);
	}
}
