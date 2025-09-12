package com.dietiestates.api.exception.notfound;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class OfferNotFoundException extends AppException {

	public OfferNotFoundException() {
		super(BusinessErrorCodes.OFFER_NOT_FOUND);
	}
}
