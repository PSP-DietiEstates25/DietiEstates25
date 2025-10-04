package com.dietiestates.api.finder;

import javax.management.relation.RoleNotFoundException;

import com.dietiestates.api.model.Role;

public interface RoleFinder {

	Role getByRoleName(String roleName)
			throws RoleNotFoundException;
}
