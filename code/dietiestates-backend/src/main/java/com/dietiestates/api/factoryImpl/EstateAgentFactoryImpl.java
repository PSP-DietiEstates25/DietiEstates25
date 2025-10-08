package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.factory.EstateAgentFactory;
import com.dietiestates.api.model.Admin;
import com.dietiestates.api.model.DefaultAccount;
import com.dietiestates.api.model.EstateAgent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EstateAgentFactoryImpl implements EstateAgentFactory {

	@Override
	public EstateAgent createEstateAgentFromSpec(
			DefaultAccount securityAccoutDecorator,
			Admin admin
			) {
		return EstateAgent.builder()
				.securityAccountDecorator(securityAccoutDecorator)
				.admin(admin)
				.build();
	}
	
}
