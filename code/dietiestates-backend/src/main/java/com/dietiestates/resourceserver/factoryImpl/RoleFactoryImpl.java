package com.dietiestates.resourceserver.factoryImpl;

import com.dietiestates.resourceserver.factory.RoleFactory;
import com.dietiestates.resourceserver.model.Role;

public class RoleFactoryImpl implements RoleFactory {

	@Override
	public Role createRoleFromSpec(String name) {
		return Role.builder()
				.name(name)
				.build();
	}

}
