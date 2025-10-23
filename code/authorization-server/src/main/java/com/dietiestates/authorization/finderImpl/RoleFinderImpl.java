package com.dietiestates.authorization.finderImpl;

import javax.management.relation.RoleNotFoundException;

import org.springframework.stereotype.Component;

import com.dietiestates.authorization.enums.RoleName;
import com.dietiestates.authorization.finder.RoleFinder;
import com.dietiestates.authorization.model.Role;
import com.dietiestates.authorization.repository.RoleRepository;

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
