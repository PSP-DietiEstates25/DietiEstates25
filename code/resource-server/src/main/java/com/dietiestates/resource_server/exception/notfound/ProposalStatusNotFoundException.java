package com.dietiestates.resource_server.exception.notfound;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class ProposalStatusNotFoundException extends AppException {
    public ProposalStatusNotFoundException() {
        super(BusinessErrorCodes.PROPOSAL_STATUS_NOT_FOUND);
    }
}
