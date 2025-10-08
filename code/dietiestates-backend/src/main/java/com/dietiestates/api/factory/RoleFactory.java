package com.dietiestates.api.factory;

import com.dietiestates.api.model.Role;

public interface RoleFactory {

	Role createRoleFromSpec(
			String name
			);
}
