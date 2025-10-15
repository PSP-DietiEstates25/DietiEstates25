package com.authenticationserver.api.factory;

import com.authenticationserver.api.model.Role;

public interface RoleFactory {

	Role createRoleFromSpec(
			String name
			);
}
