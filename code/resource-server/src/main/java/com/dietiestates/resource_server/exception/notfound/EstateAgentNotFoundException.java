package com.dietiestates.resource_server.exception.notfound;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class EstateAgentNotFoundException extends AppException {

    public EstateAgentNotFoundException() {
        super(BusinessErrorCodes.ESTATE_AGENT_NOT_FOUND);
    }
}
