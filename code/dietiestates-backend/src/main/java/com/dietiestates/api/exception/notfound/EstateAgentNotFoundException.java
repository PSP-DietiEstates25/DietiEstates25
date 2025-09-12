package com.dietiestates.api.exception.notfound;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class EstateAgentNotFoundException extends AppException {

	public EstateAgentNotFoundException() {
		super(BusinessErrorCodes.ESTATE_AGENT_NOT_FOUND);
	}
}
