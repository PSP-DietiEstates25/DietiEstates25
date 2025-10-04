package com.dietiestates.api.mapperimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.StafferRequest;
import com.dietiestates.api.mapper.AdminMapper;
import com.dietiestates.api.spec.StafferSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminMapperImpl implements AdminMapper {
	
	@Override
	public StafferSpec toSpec(StafferRequest request) {
		return StafferSpec.stafferSpecBuilder()
				.email(request.getEmail())
				.password(request.getPassword())
				.accountLocked(false)
				.enabled(true)
				.role(request.getRole())
				.adminEmail(request.getAdminEmail())
				.build();
	}
}
