package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.RoleNotFoundException;
import com.dietiestates.resource_server.model.Role;

public interface RoleFinder {

	Role getByRoleName(String roleName)
			throws RoleNotFoundException;
}
