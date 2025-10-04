package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.StafferRequest;
import com.dietiestates.api.factory.EstateAgentAuthenticationFactory;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EstateAgentAuthenticationFactoryImpl extends AuthenticationFactoryImpl implements EstateAgentAuthenticationFactory {

	@Override
	public void register(StafferRequest request) {
		
	}
	
}
