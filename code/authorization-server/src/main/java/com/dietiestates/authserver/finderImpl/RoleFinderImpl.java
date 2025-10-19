package com.dietiestates.authserver.finderImpl;

import javax.management.relation.RoleNotFoundException;

import org.springframework.stereotype.Component;

import com.dietiestates.authserver.enums.RoleName;
import com.dietiestates.authserver.finder.RoleFinder;
import com.dietiestates.authserver.model.Role;
import com.dietiestates.authserver.repository.RoleRepository;

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
