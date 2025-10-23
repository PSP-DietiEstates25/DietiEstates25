package com.dietiestates.authorization.factory;

import com.dietiestates.authorization.model.Role;

public interface RoleFactory {

	Role createRoleFromSpec(
			String name
			);
}
