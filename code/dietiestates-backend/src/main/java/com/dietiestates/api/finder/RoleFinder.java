package com.dietiestates.api.finder;

import com.dietiestates.api.exception.notfound.RoleNotFoundException;
import com.dietiestates.api.model.Role;

public interface RoleFinder {

	Role getByRoleName(String roleName)
			throws RoleNotFoundException;
}
