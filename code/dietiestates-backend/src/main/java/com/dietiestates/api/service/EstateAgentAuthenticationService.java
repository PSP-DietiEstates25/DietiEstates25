package com.dietiestates.api.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.management.relation.RoleNotFoundException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.StafferRequest;
import com.dietiestates.api.exception.notfound.AdminNotFoundException;
import com.dietiestates.api.finder.AdminFinder;
import com.dietiestates.api.finder.EstateAgentFinder;
import com.dietiestates.api.finder.RoleFinder;
import com.dietiestates.api.model.EstateAgent;
import com.dietiestates.api.repository.AdminRepository;
import com.dietiestates.api.repository.EstateAgentRepository;
import com.dietiestates.api.repository.UserRepository;
import com.dietiestates.api.security.JwtService;

@Service
public class EstateAgentAuthenticationService extends AuthenticationService {

	private final EstateAgentRepository estateAgentRepository;
	
	private final AdminFinder adminFinder;
	private final EstateAgentFinder estateAgentFinder;

	public EstateAgentAuthenticationService(
			RoleFinder roleFinder,
			UserRepository userRepository,
			PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, 
			JwtService jwtService,
			EstateAgentRepository estateAgentRepository,
			AdminFinder adminFinder,
			EstateAgentFinder estateAgentFinder
			) {
		super(roleFinder, userRepository, passwordEncoder, authenticationManager, jwtService);
		this.estateAgentRepository = estateAgentRepository;
		this.adminFinder = adminFinder;
		this.estateAgentFinder = estateAgentFinder;
	}

	public void register(StafferRequest request) throws RoleNotFoundException {
		var estateAgent = of(request);
		estateAgentRepository.save(estateAgent);
	}
	
	public EstateAgent of(StafferRequest request) throws RoleNotFoundException {
		
		var estateAgentRole = roleFinder.getByRoleName("ESTATE_AGENT");
		var admin = adminFinder.getAdminByEmail(request.getAdminEmail());
		
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
