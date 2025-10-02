package com.dietiestates.api.exception.notowned;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class CadastralDataNotOwnedByRealEstateException extends AppException {

	public CadastralDataNotOwnedByRealEstateException(){
		super(BusinessErrorCodes.CADASTRAL_DATA_NOT_OWNED_BY_REAL_ESTATE);
	}
}
