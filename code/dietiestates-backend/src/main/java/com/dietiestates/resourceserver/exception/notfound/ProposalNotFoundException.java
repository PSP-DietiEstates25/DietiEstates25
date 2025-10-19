package com.dietiestates.resourceserver.exception.notfound;

import com.dietiestates.resourceserver.enums.BusinessErrorCodes;
import com.dietiestates.resourceserver.exception.AppException;

public class ProposalNotFoundException extends AppException {

	public ProposalNotFoundException() {
		super(BusinessErrorCodes.PROPOSAL_NOT_FOUND);
	}
}
