package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.StafferRequest;
import com.dietiestates.resource_server.dto.response.EstateAgentResponse;
import com.dietiestates.resource_server.exception.notfound.EstateAgentNotFoundException;
import com.dietiestates.resource_server.exception.notfound.RoleNotFoundException;
import com.dietiestates.resource_server.model.Staffer;

public interface EstateAgentService {

	EstateAgentResponse register(StafferRequest request) throws RoleNotFoundException;

    EstateAgentResponse getEstateAgentById(Long estateAgentId) throws EstateAgentNotFoundException;
}
