package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.StafferRequest;
import com.dietiestates.api.factory.AdminFactory;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminFactoryImpl extends AuthenticationFactoryImpl implements AdminFactory {
	
	@Override
	public void register(StafferRequest request) {
		
	}
}
