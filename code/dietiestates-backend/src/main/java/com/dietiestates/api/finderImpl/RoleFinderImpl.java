package com.dietiestates.api.finderImpl;

import javax.management.relation.RoleNotFoundException;

import org.springframework.stereotype.Component;

import com.dietiestates.api.finder.RoleFinder;
import com.dietiestates.api.model.Role;
import com.dietiestates.api.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleFinderImpl implements RoleFinder {

	private final RoleRepository roleRepository;
	
	@Override
	public Role getByRoleName(String roleName)
			throws RoleNotFoundException {
		return roleRepository.findByName(roleName)
				.orElseThrow(RoleNotFoundException::new);
	}

}
