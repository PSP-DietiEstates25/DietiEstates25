package com.dietiestates.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dietiestates.api.model.Admin;
import com.dietiestates.api.model.DefaultAccount;
import com.dietiestates.api.model.SecurityAccountDecorator;
import com.dietiestates.api.repository.AdminRepository;
import com.dietiestates.api.repository.DefaultAccountRepository;
import com.dietiestates.api.repository.RoleRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@DependsOn("rolesConfig")
@RequiredArgsConstructor
public class AdminConfig {

	private final RoleRepository roleRepository;
	private final DefaultAccountRepository defaultAccountRepository;
	private final AdminRepository adminRepository;
	private final PasswordEncoder passwordEncoder;
	
	@PostConstruct
	public void init() {
		if(defaultAccountRepository.findByEmail("admin@admin.com").isEmpty()) {

			var defaultAccount = DefaultAccount.builder()
					.email("admin@admin.com")
					.password(passwordEncoder.encode("adminpassword"))
					.role(roleRepository.findByName("ADMIN").get())
					.build();
			var securityAccountDecorator = SecurityAccountDecorator.builder()
					.defaultAccount(defaultAccount)
					.enabled(true)
					.locked(false)
					.build();
			var admin = new Admin();
			admin.setSecurityAccountDecorator(defaultAccount);
			admin.setAdmin(admin);
			
			defaultAccountRepository.save(defaultAccount);
			adminRepository.save(admin);
		}
	}
	/*
	@Bean
	public InitializingBean initializeSuperAdmin(
			RoleRepository roleRepository,
			DefaultAccountRepository defaultAccountRepository,
			AdminRepository adminRepository,
			PasswordEncoder passwordEncoder
			) {
		return () -> {
			if(defaultAccountRepository.findByEmail("admin@admin.com").isEmpty()) {

				var defaultAccount = DefaultAccount.builder()
						.email("admin@admin.com")
						.password(passwordEncoder.encode("adminpassword"))
						.role(roleRepository.findByName("ADMIN").get())
						.build();
				var securityAccountDecorator = SecurityAccountDecorator.builder()
						.defaultAccount(defaultAccount)
						.enabled(true)
						.locked(false)
						.build();
				var admin = new Admin();
				admin.setSecurityAccountDecorator(defaultAccount);
				admin.setAdmin(admin);
				
				defaultAccountRepository.save(defaultAccount);
				adminRepository.save(admin);
			};
		};
	}
	*/
}