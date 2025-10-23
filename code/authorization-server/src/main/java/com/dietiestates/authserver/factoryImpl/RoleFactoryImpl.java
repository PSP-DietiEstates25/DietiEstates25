package com.dietiestates.authserver.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.authserver.factory.RoleFactory;
import com.dietiestates.authserver.model.Role;

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
