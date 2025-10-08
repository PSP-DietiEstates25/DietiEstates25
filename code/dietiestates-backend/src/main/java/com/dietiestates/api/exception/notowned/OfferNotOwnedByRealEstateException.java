package com.dietiestates.api.exception.notowned;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class OfferNotOwnedByRealEstateException extends AppException {

	private static final long serialVersionUID = 8002736732369411760L;

	public OfferNotOwnedByRealEstateException() {
		super(BusinessErrorCodes.OFFER_NOT_OWNED_BY_REAL_ESTATE);
	}
}
