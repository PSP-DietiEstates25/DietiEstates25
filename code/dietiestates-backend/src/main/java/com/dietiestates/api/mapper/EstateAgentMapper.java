package com.dietiestates.api.mapper;

import com.dietiestates.api.dto.request.StafferRequest;
import com.dietiestates.api.spec.StafferSpec;

public class EstateAgentMapper {

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
