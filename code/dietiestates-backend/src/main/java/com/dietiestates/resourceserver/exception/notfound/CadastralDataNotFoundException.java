package com.dietiestates.resourceserver.exception.notfound;

import com.dietiestates.resourceserver.enums.BusinessErrorCodes;
import com.dietiestates.resourceserver.exception.AppException;

public class CadastralDataNotFoundException extends AppException {

	public CadastralDataNotFoundException() {
		super(BusinessErrorCodes.CADASTRAL_DATA_NOT_FOUND);
	}
}
