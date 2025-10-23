package com.dietiestates.authorization.exception.alreadyexists;

import com.dietiestates.authorization.enums.BusinessErrorCodes;
import com.dietiestates.authorization.exception.AppException;

public class AccountAlreadyExistsException extends AppException {

	private static final long serialVersionUID = 1075231391306617908L;

	public AccountAlreadyExistsException() {
		super(BusinessErrorCodes.ACCOUNT_ALREADY_EXISTS);
	}
}
