package com.dietiestates.api.finder;

import com.dietiestates.api.exception.notfound.EstateAgentNotFoundException;
import com.dietiestates.api.model.EstateAgent;

public interface EstateAgentFinder {

	EstateAgent getEstateAgentByEmail(String agentEmail)
			throws EstateAgentNotFoundException;
}
