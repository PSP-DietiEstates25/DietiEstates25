package com.authenticationserver.api.finderImpl;

import javax.management.relation.RoleNotFoundException;

import org.springframework.stereotype.Component;

import com.authenticationserver.api.enums.RoleName;
import com.authenticationserver.api.finder.RoleFinder;
import com.authenticationserver.api.model.Role;
import com.authenticationserver.api.repository.RoleRepository;

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
