package com.dietiestates.api.service;

import javax.management.relation.RoleNotFoundException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.StafferRequest;
import com.dietiestates.api.finder.AdminFinder;
import com.dietiestates.api.finder.EstateAgentFinder;
import com.dietiestates.api.finder.RoleFinder;
import com.dietiestates.api.finder.UserFinder;
import com.dietiestates.api.mapper.AdminMapper;
import com.dietiestates.api.repository.AdminRepository;
import com.dietiestates.api.repository.UserRepository;
import com.dietiestates.api.security.JwtService;

@Service
public class AdminAuthenticationService extends AuthenticationService {
	
	private final AdminRepository adminRepository;
	private final AdminFinder adminFinder;
	private final AdminMapper adminMapper;

	public AdminAuthenticationService(
			RoleFinder roleFinder,
			UserRepository userRepository,
			PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager,
			JwtService jwtService,
			AdminRepository adminRepository,
			UserFinder userFinder,
			EstateAgentFinder estateAgentFinder,
			AdminFinder adminFinder,
			AdminMapper adminMapper
			) {
		super(roleFinder, userRepository, passwordEncoder, authenticationManager, jwtService);
		this.adminRepository = adminRepository;
		this.adminFinder = adminFinder;
		this.adminMapper = adminMapper;
	}
	
	public void register(StafferRequest request) throws RoleNotFoundException {
		
		var adminRole = roleFinder.getByRoleName("ADMIN");
		var creator = adminFinder.getAdminByEmail(request.getAdminEmail());
		
		var admin = adminMapper.toEntity(request, passwordEncoder, adminRole, creator);
		adminRepository.save(admin);
	}
}

