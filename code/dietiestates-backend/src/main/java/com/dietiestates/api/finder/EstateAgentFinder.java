package com.dietiestates.api.finder;

import java.util.List;

import com.dietiestates.api.exception.notfound.EstateAgentNotFoundException;
import com.dietiestates.api.model.EstateAgent;

public interface EstateAgentFinder {

	EstateAgent getEstateAgentByEmail(String agentEmail)
			throws EstateAgentNotFoundException;
	
	List<EstateAgent> getAllEstateAgents();
}
