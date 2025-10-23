package com.dietiestates.resourceserver.exception.notowned;

import com.dietiestates.resourceserver.enums.BusinessErrorCodes;
import com.dietiestates.resourceserver.exception.AppException;

public class OfferNotOwnedByRealEstateException extends AppException {

	private static final long serialVersionUID = 8002736732369411760L;

	public OfferNotOwnedByRealEstateException() {
		super(BusinessErrorCodes.OFFER_NOT_OWNED_BY_REAL_ESTATE);
	}
}
