package com.dietiestates.authorization.finder;

import javax.management.relation.RoleNotFoundException;

import com.dietiestates.authorization.model.Role;

public interface RoleFinder {
	
	Role getByRoleName(String roleName)
			throws RoleNotFoundException;
}
