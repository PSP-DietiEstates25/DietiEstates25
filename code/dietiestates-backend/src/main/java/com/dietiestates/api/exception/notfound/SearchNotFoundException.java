package com.dietiestates.api.exception.notfound;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class SearchNotFoundException extends AppException {
	
	public SearchNotFoundException() {
		super(BusinessErrorCodes.SEARCH_NOT_FOUND);
	}
}
