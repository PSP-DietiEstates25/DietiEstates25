package com.dietiestates.api.factory;

import com.dietiestates.api.dto.request.StafferRequest;

public interface EstateAgentAuthenticationFactory extends AuthenticationFactory {

	void register(StafferRequest request);
	
}
