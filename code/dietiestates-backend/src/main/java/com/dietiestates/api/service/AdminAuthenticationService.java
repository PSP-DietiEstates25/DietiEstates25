package com.dietiestates.api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.StafferRequest;
import com.dietiestates.api.exception.notfound.AdminNotFoundException;
import com.dietiestates.api.model.Admin;
import com.dietiestates.api.repository.AdminRepository;
import com.dietiestates.api.repository.EstateAgentRepository;
import com.dietiestates.api.repository.RoleRepository;
import com.dietiestates.api.repository.UserRepository;
import com.dietiestates.api.security.JwtService;

@Service
public class AdminAuthenticationService extends AuthenticationService {

	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final AdminRepository adminRepository;

	public AdminAuthenticationService(
			RoleRepository roleRepository, 
			PasswordEncoder passwordEncoder,
			UserRepository userRepository,
			EstateAgentRepository estateAgentRepository,
			AdminRepository adminRepository,
			AuthenticationManager authenticationManager, 
			JwtService jwtService,
			AuthenticationService authenticationService) {
		super(roleRepository, passwordEncoder, userRepository, authenticationManager, jwtService);
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.adminRepository = adminRepository;
	}
	
	public void register(StafferRequest request) {
		var admin = of(request);
		adminRepository.save(admin);
	}
	
	public Admin of(StafferRequest request) {
		var adminRole = roleRepository.findByName("ADMIN")
				.orElseThrow(() -> new IllegalStateException("ROLE ADMIN was not initialized!"));
		
		var admin = adminRepository.findByEmail(request.getAdminEmail())
				.orElseThrow(AdminNotFoundException::new);
		
		return Admin.adminBuilder()
				.createdDate(LocalDateTime.now())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.accountLocked(false)
				.enabled(true)
				.roles(List.of(adminRole))
				.admin(admin)
				.build();
	}
}
