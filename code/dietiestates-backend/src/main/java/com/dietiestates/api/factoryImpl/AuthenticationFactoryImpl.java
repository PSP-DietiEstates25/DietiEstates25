package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.AuthenticationRequest;
import com.dietiestates.api.dto.response.AuthenticationResponse;
import com.dietiestates.api.factory.AuthenticationFactory;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthenticationFactoryImpl implements AuthenticationFactory {

	@Override
	public void register(AuthenticationRequest request) {
		
	}
	
	@Override
	public AuthenticationResponse Login(AuthenticationRequest request) {
		return AuthenticationResponse.builder().build();
	}
}
