package com.dietiestates.api.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.StafferRequest;
import com.dietiestates.api.exception.notfound.AdminNotFoundException;
import com.dietiestates.api.mapper.AdminMapper;
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
	private final AdminMapper adminMapper;

	public AdminAuthenticationService(
			RoleRepository roleRepository, 
			PasswordEncoder passwordEncoder,
			UserRepository userRepository,
			EstateAgentRepository estateAgentRepository,
			AdminRepository adminRepository,
			AuthenticationManager authenticationManager,
			AdminMapper adminMapper,
			JwtService jwtService,
			AuthenticationService authenticationService) {
		super(roleRepository, passwordEncoder, userRepository, authenticationManager, jwtService);
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.adminRepository = adminRepository;
		this.adminMapper = adminMapper;
	}
	
	public void register(StafferRequest request) {
		
		var adminRole = roleRepository.findByName("ADMIN")
				.orElseThrow(() -> new IllegalStateException("ROLE ADMIN was not initialized!"));
		
		var creator = adminRepository.findByEmail(request.getAdminEmail())
				.orElseThrow(AdminNotFoundException::new);
		
		var admin = adminMapper.toEntity(request, passwordEncoder, adminRole, creator);
		adminRepository.save(admin);
	}
}

