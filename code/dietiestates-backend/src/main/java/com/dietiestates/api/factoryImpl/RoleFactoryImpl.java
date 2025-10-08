package com.dietiestates.api.factoryImpl;

import com.dietiestates.api.factory.RoleFactory;
import com.dietiestates.api.model.Role;

public class RoleFactoryImpl implements RoleFactory {

	@Override
	public Role createRoleFromSpec(String name) {
		return Role.builder()
				.name(name)
				.build();
	}

}
