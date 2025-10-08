package com.dietiestates.api.factory;

import com.dietiestates.api.model.Admin;
import com.dietiestates.api.model.DefaultAccount;
import com.dietiestates.api.model.EstateAgent;

public interface EstateAgentFactory {

	EstateAgent createEstateAgentFromSpec(
			DefaultAccount securityAccountDecorator,
			Admin admin
			);
	
}
