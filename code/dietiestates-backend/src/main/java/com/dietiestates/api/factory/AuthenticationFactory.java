package com.dietiestates.api.factory;

import com.dietiestates.api.dto.request.AuthenticationRequest;
import com.dietiestates.api.dto.response.AuthenticationResponse;

public interface AuthenticationFactory {

	void register(AuthenticationRequest request);
	
	AuthenticationResponse Login(AuthenticationRequest request);
	
}
