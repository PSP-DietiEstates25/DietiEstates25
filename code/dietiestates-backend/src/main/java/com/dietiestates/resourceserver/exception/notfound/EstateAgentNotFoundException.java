package com.dietiestates.resourceserver.exception.notfound;

import com.dietiestates.resourceserver.enums.BusinessErrorCodes;
import com.dietiestates.resourceserver.exception.AppException;

public class EstateAgentNotFoundException extends AppException {

	public EstateAgentNotFoundException() {
		super(BusinessErrorCodes.ESTATE_AGENT_NOT_FOUND);
	}
}
