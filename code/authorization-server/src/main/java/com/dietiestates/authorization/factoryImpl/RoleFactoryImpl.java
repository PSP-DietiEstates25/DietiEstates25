package com.dietiestates.authorization.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.authorization.factory.RoleFactory;
import com.dietiestates.authorization.model.Role;

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
