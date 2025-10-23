package com.dietiestates.authserver.finder;

import javax.management.relation.RoleNotFoundException;

import com.dietiestates.authserver.model.Role;

public interface RoleFinder {
	
	Role getByRoleName(String roleName)
			throws RoleNotFoundException;
}
