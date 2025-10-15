package com.authenticationserver.api.config;

import org.springframework.stereotype.Component;

import com.authenticationserver.api.enums.RoleName;
import com.authenticationserver.api.model.Role;
import com.authenticationserver.api.repository.RoleRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RolesConfig {

	private final RoleRepository roleRepository;
	
	@PostConstruct
	public void init() {
		if(roleRepository.findByName(RoleName.valueOf("ROLE_USER")).isEmpty()) {
			roleRepository.save(
					Role.builder().name("ROLE_USER").build()
					);
		}
		
		if(roleRepository.findByName(RoleName.valueOf("ROLE_ADMIN")).isEmpty()) {
			roleRepository.save(
					Role.builder().name("ROLE_ADMIN").build()
					);
		}
		
		if(roleRepository.findByName(RoleName.valueOf("ROLE_ESTATE_AGENT")).isEmpty()) {
			roleRepository.save(
					Role.builder().name("ROLE_ESTATE_AGENT").build()
					);
		}
	}
}
