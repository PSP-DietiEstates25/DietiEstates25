package com.dietiestates.api.exception.notowned;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class CadastralFilterNotOwnedBySearchException extends AppException {
	
	private static final long serialVersionUID = -5111554009412342855L;

	public CadastralFilterNotOwnedBySearchException() {
		super(BusinessErrorCodes.CADASTRAL_FILTER_NOT_OWNED_BY_SEARCH);
	}
}
