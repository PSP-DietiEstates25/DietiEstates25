package com.dietiestates.resourceserver.mapperimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.dto.request.AuthenticationRequest;
import com.dietiestates.resourceserver.mapper.UserMapper;
import com.dietiestates.resourceserver.spec.AuthenticationSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

	@Override
	public AuthenticationSpec toSpec(AuthenticationRequest request) {
		return AuthenticationSpec.builder()
				.email(request.getEmail())
				.build();
	}
}
