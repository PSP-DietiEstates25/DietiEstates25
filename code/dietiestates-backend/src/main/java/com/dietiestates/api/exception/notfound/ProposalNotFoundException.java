package com.dietiestates.api.exception.notfound;

import com.dietiestates.api.enums.BusinessErrorCodes;
import com.dietiestates.api.exception.AppException;

public class ProposalNotFoundException extends AppException {

	public ProposalNotFoundException() {
		super(BusinessErrorCodes.PROPOSAL_NOT_FOUND);
	}
}
