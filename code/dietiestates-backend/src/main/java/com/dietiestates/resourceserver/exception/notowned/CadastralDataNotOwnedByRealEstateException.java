package com.dietiestates.resourceserver.exception.notowned;

import com.dietiestates.resourceserver.enums.BusinessErrorCodes;
import com.dietiestates.resourceserver.exception.AppException;

public class CadastralDataNotOwnedByRealEstateException extends AppException {

	private static final long serialVersionUID = -7492089153966644911L;

	public CadastralDataNotOwnedByRealEstateException(){
		super(BusinessErrorCodes.CADASTRAL_DATA_NOT_OWNED_BY_REAL_ESTATE);
	}
}
