package com.authenticationserver.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.authenticationserver.api.factory.RoleFactory;
import com.authenticationserver.api.model.Role;

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
