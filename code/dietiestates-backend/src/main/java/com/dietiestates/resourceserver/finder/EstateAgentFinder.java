package com.dietiestates.resourceserver.finder;

import java.util.List;

import com.dietiestates.resourceserver.exception.notfound.EstateAgentNotFoundException;
import com.dietiestates.resourceserver.model.EstateAgent;

public interface EstateAgentFinder {

	EstateAgent getEstateAgentByEmail(String agentEmail)
			throws EstateAgentNotFoundException;
	
	List<EstateAgent> getAllEstateAgents();
}
