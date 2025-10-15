package com.authorizationserver.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.authorizationserver.api.factory.RoleFactory;
import com.authorizationserver.api.model.Role;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleFactoryImpl implements RoleFactory {

	@Override
	public Role createRoleFromSpec(String name) {
		return Role.builder()
				.name(name)
				.build();
	}
}
