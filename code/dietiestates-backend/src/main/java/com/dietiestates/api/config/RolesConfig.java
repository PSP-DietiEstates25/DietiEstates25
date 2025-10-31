package com.dietiestates.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dietiestates.api.model.Role;
import com.dietiestates.api.repository.RoleRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RolesConfig {

	private final RoleRepository roleRepository;
	
	@PostConstruct
	public void init() {
		if(roleRepository.findByName("USER").isEmpty()) {
			roleRepository.save(
					Role.builder().name("USER").build()
					);
		}
		
		if(roleRepository.findByName("ADMIN").isEmpty()) {
			roleRepository.save(
					Role.builder().name("ADMIN").build()
					);
		}
		
		if(roleRepository.findByName("ESTATE_AGENT").isEmpty()) {
			roleRepository.save(
					Role.builder().name("ESTATE_AGENT").build()
					);
		}
	}
}
