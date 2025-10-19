package com.dietiestates.resourceserver.finder;

import com.dietiestates.resourceserver.exception.notfound.RoleNotFoundException;
import com.dietiestates.resourceserver.model.Role;

public interface RoleFinder {

	Role getByRoleName(String roleName)
			throws RoleNotFoundException;
}
