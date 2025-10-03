package com.dietiestates.api.exception.notfound;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class CadastralDataNotFoundException extends AppException {

	public CadastralDataNotFoundException() {
		super(BusinessErrorCodes.CADASTRAL_DATA_NOT_FOUND);
	}
}
