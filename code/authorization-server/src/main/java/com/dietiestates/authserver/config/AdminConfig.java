package com.dietiestates.authserver.config;

import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dietiestates.authserver.enums.RoleName;
import com.dietiestates.authserver.model.DefaultAccount;
import com.dietiestates.authserver.model.SecurityAccountDecorator;
import com.dietiestates.authserver.repository.DefaultAccountRepository;
import com.dietiestates.authserver.repository.RoleRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@DependsOn("rolesConfig")
@RequiredArgsConstructor
public class AdminConfig {

	private final RoleRepository roleRepository;
	private final DefaultAccountRepository defaultAccountRepository;
	private final PasswordEncoder passwordEncoder;
	
	@PostConstruct
	public void init() {
		if(defaultAccountRepository.findByEmail("admin@admin.com").isEmpty()) {

			var defaultAccount = DefaultAccount.builder()
					.email("admin@admin.com")
					.password(passwordEncoder.encode("adminpassword"))
					.role(roleRepository.findByName(RoleName.ADMIN).get())
					.build();
			var securityAccountDecorator = SecurityAccountDecorator.builder()
					.defaultAccount(defaultAccount)
					.enabled(true)
					.locked(false)
					.build();
			
			defaultAccountRepository.save(defaultAccount);
		}
	}
}
