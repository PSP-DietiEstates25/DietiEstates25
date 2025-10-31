package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.RoleNotFoundException;
import com.dietiestates.resource_server.finder.RoleFinder;
import com.dietiestates.resource_server.model.Role;
import com.dietiestates.resource_server.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleFinderDefaultImpl implements RoleFinder {

	private final RoleRepository roleRepository;
	
	@Override
	public Role getByRoleName(String roleName)
			throws RoleNotFoundException {
		return roleRepository.findByName(roleName)
				.orElseThrow(RoleNotFoundException::new);
	}

}
