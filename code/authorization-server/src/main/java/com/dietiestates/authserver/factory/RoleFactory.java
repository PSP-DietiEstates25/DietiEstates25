package com.dietiestates.authserver.factory;

import com.dietiestates.authserver.model.Role;

public interface RoleFactory {

	Role createRoleFromSpec(
			String name
			);
}
