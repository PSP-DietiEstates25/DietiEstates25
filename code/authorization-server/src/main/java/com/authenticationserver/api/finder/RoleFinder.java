package com.authenticationserver.api.finder;

import javax.management.relation.RoleNotFoundException;

import com.authenticationserver.api.model.Role;

public interface RoleFinder {
	
	Role getByRoleName(String roleName)
			throws RoleNotFoundException;
}
