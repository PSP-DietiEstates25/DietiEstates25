package com.dietiestates.api.mapperimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.AuthenticationRequest;
import com.dietiestates.api.mapper.UserMapper;
import com.dietiestates.api.spec.AuthenticationSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

	@Override
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
