package com.dietiestates.resourceserver.factory;

import com.dietiestates.resourceserver.model.Admin;
import com.dietiestates.resourceserver.model.EstateAgent;

public interface EstateAgentFactory {

	EstateAgent createEstateAgentFromSpec(
			String email,
			Admin admin
			);
	
}
