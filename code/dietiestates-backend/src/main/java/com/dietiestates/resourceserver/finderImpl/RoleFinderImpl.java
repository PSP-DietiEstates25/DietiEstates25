package com.dietiestates.resourceserver.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.exception.notfound.RoleNotFoundException;
import com.dietiestates.resourceserver.finder.RoleFinder;
import com.dietiestates.resourceserver.model.Role;
import com.dietiestates.resourceserver.repository.RoleRepository;

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
