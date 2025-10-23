package com.dietiestates.resourceserver.factory;

import com.dietiestates.resourceserver.model.Role;

public interface RoleFactory {

	Role createRoleFromSpec(
			String name
			);
}
