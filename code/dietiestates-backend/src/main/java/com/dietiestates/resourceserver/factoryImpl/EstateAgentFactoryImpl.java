package com.dietiestates.resourceserver.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.factory.EstateAgentFactory;
import com.dietiestates.resourceserver.model.Admin;
import com.dietiestates.resourceserver.model.EstateAgent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EstateAgentFactoryImpl implements EstateAgentFactory {

	@Override
	public EstateAgent createEstateAgentFromSpec(
			String email,
			Admin admin
			) {
		return EstateAgent.builder()
				.email(email)
				.admin(admin)
				.build();
	}
	
}
