package com.dietiestates.resourceserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.model.Admin;
import com.dietiestates.resourceserver.repository.AdminRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@DependsOn("rolesConfig")
@RequiredArgsConstructor
public class AdminConfig {

	@Value("${defaultAdminEmail}")
	private String defaultAdminEmail;
	
	private final AdminRepository adminRepository;
	
	@PostConstruct
	public void init() {
		if(adminRepository.findByEmail(defaultAdminEmail).isEmpty()) {
			
			var admin = new Admin();
			admin.setEmail(defaultAdminEmail);
			admin.setAdmin(admin);
			
			adminRepository.save(admin);
		}
	}
}