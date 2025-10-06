package com.dietiestates.api.serviceImpl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.StafferRequest;
import com.dietiestates.api.exception.notfound.RoleNotFoundException;
import com.dietiestates.api.factory.AuthenticationFactory;
import com.dietiestates.api.factory.EstateAgentFactory;
import com.dietiestates.api.finder.AdminFinder;
import com.dietiestates.api.finder.EstateAgentFinder;
import com.dietiestates.api.finder.RoleFinder;
import com.dietiestates.api.mapper.EstateAgentMapper;
import com.dietiestates.api.mapper.UserMapper;
import com.dietiestates.api.repository.EstateAgentRepository;
import com.dietiestates.api.repository.UserRepository;
import com.dietiestates.api.security.JwtService;
import com.dietiestates.api.service.EstateAgentAuthenticationService;

@Service
public class EstateAgentAuthenticationServiceImpl extends AuthenticationServiceImpl implements EstateAgentAuthenticationService {

	private final EstateAgentRepository estateAgentRepository;
	private final EstateAgentFactory estateAgentFactory;
	private final AdminFinder adminFinder;
	private final EstateAgentFinder estateAgentFinder;
	private final EstateAgentMapper estateAgentMapper;

	public EstateAgentAuthenticationServiceImpl(
			RoleFinder roleFinder,
			UserRepository userRepository,
			UserMapper userMapper,
			AuthenticationFactory authenticationFactory,
			PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, 
			JwtService jwtService,
			EstateAgentRepository estateAgentRepository,
			EstateAgentFactory estateAgentFactory,
			AdminFinder adminFinder,
			EstateAgentFinder estateAgentFinder,
			EstateAgentMapper estateAgentMapper
			) {
		super(roleFinder, userRepository, userMapper, authenticationFactory, passwordEncoder, authenticationManager, jwtService);
		this.estateAgentRepository = estateAgentRepository;
		this.estateAgentFactory = estateAgentFactory;
		this.adminFinder = adminFinder;
		this.estateAgentFinder = estateAgentFinder;
		this.estateAgentMapper = estateAgentMapper;
	}

	@Override
	public void register(StafferRequest request) throws RoleNotFoundException {
		
		var stafferSpec = estateAgentMapper.toSpec(request);
		var estateAgentRole = roleFinder.getByRoleName("ESTATE_AGENT");
		var creator = adminFinder.getAdminByEmail(stafferSpec.getAdminEmail());
				
		var estateAgent = estateAgentFactory.createEstateAgentFromSpec(stafferSpec, passwordEncoder, estateAgentRole, creator);
		estateAgentRepository.save(estateAgent);
	}

}
