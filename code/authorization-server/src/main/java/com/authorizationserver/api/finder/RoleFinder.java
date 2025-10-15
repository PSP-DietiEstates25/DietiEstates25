package com.authorizationserver.api.finder;

import javax.management.relation.RoleNotFoundException;

import com.authorizationserver.api.model.Role;

public interface RoleFinder {
	
	Role getByRoleName(String roleName)
			throws RoleNotFoundException;
}
