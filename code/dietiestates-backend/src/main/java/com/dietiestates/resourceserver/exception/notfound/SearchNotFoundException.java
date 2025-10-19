package com.dietiestates.resourceserver.exception.notfound;

import com.dietiestates.resourceserver.enums.BusinessErrorCodes;
import com.dietiestates.resourceserver.exception.AppException;

public class SearchNotFoundException extends AppException {
	
	public SearchNotFoundException() {
		super(BusinessErrorCodes.SEARCH_NOT_FOUND);
	}
}
