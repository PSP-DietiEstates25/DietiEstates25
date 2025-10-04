package com.dietiestates.api.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.StafferRequest;
import com.dietiestates.api.model.Admin;
import com.dietiestates.api.model.Role;
import com.dietiestates.api.spec.StafferSpec;

@Component
public class AdminMapper {

	public Admin toEntity(
			StafferRequest request,
			PasswordEncoder passwordEncoder,
			Role adminRole,
			Admin admin
		) {
		return Admin.adminBuilder()
				.createdDate(LocalDateTime.now())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.accountLocked(false)
				.enabled(true)
				.roles(List.of(adminRole))
				.admin(admin)
				.build();
	}
	
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
