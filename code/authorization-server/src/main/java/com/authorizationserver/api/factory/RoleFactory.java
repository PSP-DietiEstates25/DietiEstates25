package com.authorizationserver.api.factory;

import com.authorizationserver.api.model.Role;

public interface RoleFactory {

	Role createRoleFromSpec(
			String name
			);
}
