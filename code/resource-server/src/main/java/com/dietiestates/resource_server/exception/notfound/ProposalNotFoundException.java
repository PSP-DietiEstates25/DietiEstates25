package com.dietiestates.resource_server.exception.notfound;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class ProposalNotFoundException extends AppException {

    public ProposalNotFoundException() {
        super(BusinessErrorCodes.PROPOSAL_NOT_FOUND);
    }
}
