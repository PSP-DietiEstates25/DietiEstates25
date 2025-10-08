package com.dietiestates.api.exception.notfound;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class AccountNotFoundException extends AppException {

	public AccountNotFoundException() {
		super(BusinessErrorCodes.ACCOUNT_NOT_FOUND);
	}
}
