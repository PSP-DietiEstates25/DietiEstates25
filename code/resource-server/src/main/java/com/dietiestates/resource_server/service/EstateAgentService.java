package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.StafferRequest;
import com.dietiestates.resource_server.exception.notfound.EstateAgentNotFoundException;
import com.dietiestates.resource_server.exception.notfound.RoleNotFoundException;

public interface EstateAgentService {

	EstateAgentResponse register(StafferRequest request, String creatorEmail) throws RoleNotFoundException;

    EstateAgentResponse getEstateAgentById(Long estateAgentId) throws EstateAgentNotFoundException;
}
