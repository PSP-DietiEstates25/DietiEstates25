package com.authorizationserver.api.finderImpl;

import javax.management.relation.RoleNotFoundException;

import org.springframework.stereotype.Component;

import com.authorizationserver.api.enums.RoleName;
import com.authorizationserver.api.finder.RoleFinder;
import com.authorizationserver.api.model.Role;
import com.authorizationserver.api.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleFinderImpl implements RoleFinder {

private final RoleRepository roleRepository;
	
	@Override
	public Role getByRoleName(String roleName)
			throws RoleNotFoundException {
		return roleRepository.findByName(RoleName.valueOf(roleName))
				.orElseThrow(RoleNotFoundException::new);
	}
}
