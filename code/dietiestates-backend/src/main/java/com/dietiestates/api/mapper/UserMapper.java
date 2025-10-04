package com.dietiestates.api.mapper;

import com.dietiestates.api.dto.request.AuthenticationRequest;
import com.dietiestates.api.spec.AuthenticationSpec;

public class UserMapper {

	public AuthenticationSpec toSpec(AuthenticationRequest request) {
		return AuthenticationSpec.builder()
				.email(request.getEmail())
				.password(request.getPassword())
				.accountLocked(false)
				.enabled(true)
				.role(request.getRole())
				.build();
	}
}
