package com.dietiestates.resource_server.finder;

import java.util.List;

import com.dietiestates.resource_server.exception.notfound.EstateAgentNotFoundException;
import com.dietiestates.resource_server.model.EstateAgent;

public interface EstateAgentFinder {

	EstateAgent getEstateAgentByEmail(String agentEmail)
			throws EstateAgentNotFoundException;

    EstateAgent getEstateAgentById(Long estateAgentId)
            throws EstateAgentNotFoundException;
	
	List<EstateAgent> getAllEstateAgents();
}
