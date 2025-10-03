package com.dietiestates.api.exception.notfound;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class CadastralFilterNotFoundException extends AppException {
	
	public CadastralFilterNotFoundException() {
		super(BusinessErrorCodes.CADASTRAL_FILTER_NOT_FOUND);
	}
}
