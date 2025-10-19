package com.dietiestates.resourceserver.exception.notfound;

import com.dietiestates.resourceserver.enums.BusinessErrorCodes;
import com.dietiestates.resourceserver.exception.AppException;

public class CadastralFilterNotFoundException extends AppException {
	
	public CadastralFilterNotFoundException() {
		super(BusinessErrorCodes.CADASTRAL_FILTER_NOT_FOUND);
	}
}
