package com.dietiestates.api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.StafferDto;
import com.dietiestates.api.exception.notfound.AdminNotFoundException;
import com.dietiestates.api.model.EstateAgent;
import com.dietiestates.api.repository.AdminRepository;
import com.dietiestates.api.repository.EstateAgentRepository;
import com.dietiestates.api.repository.RoleRepository;
import com.dietiestates.api.repository.UserRepository;
import com.dietiestates.api.security.JwtService;

@Service
public class EstateAgentAuthenticationService extends AuthenticationService {

	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final EstateAgentRepository estateAgentRepository;
	private final AdminRepository adminRepository;

	public EstateAgentAuthenticationService(
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
		this.estateAgentRepository = estateAgentRepository;
		this.adminRepository = adminRepository;
	}

	public void register(StafferDto request) {
		var estateAgent = of(request);
		estateAgentRepository.save(estateAgent);
	}
	
	public EstateAgent of(StafferDto request) {
		var estateAgentRole = roleRepository.findByName("ESTATE_AGENT")
				.orElseThrow(() -> new IllegalStateException("ROLE ESTATE_AGENT was not initialized!"));
		
		var admin = adminRepository.findByEmail(request.getAdminEmail())
				.orElseThrow(AdminNotFoundException::new);
		
		return EstateAgent.estateAgentBuilder()
				.createdDate(LocalDateTime.now())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.accountLocked(false)
				.enabled(true)
				.roles(List.of(estateAgentRole))
				.admin(admin)
				.build();
	}

}
