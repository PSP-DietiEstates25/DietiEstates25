package com.dietiestates.resourceserver.serviceImpl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dietiestates.resourceserver.dto.request.StafferRequest;
import com.dietiestates.resourceserver.exception.notfound.RoleNotFoundException;
import com.dietiestates.resourceserver.factory.AccountFactory;
import com.dietiestates.resourceserver.factory.EstateAgentFactory;
import com.dietiestates.resourceserver.factory.SecurityAccountDecoratorFactory;
import com.dietiestates.resourceserver.factory.UserFactory;
import com.dietiestates.resourceserver.finder.AdminFinder;
import com.dietiestates.resourceserver.finder.EstateAgentFinder;
import com.dietiestates.resourceserver.finder.RoleFinder;
import com.dietiestates.resourceserver.mapper.EstateAgentMapper;
import com.dietiestates.resourceserver.mapper.UserMapper;
import com.dietiestates.resourceserver.repository.DefaultAccountRepository;
import com.dietiestates.resourceserver.repository.EstateAgentRepository;
import com.dietiestates.resourceserver.repository.UserRepository;
import com.dietiestates.resourceserver.service.EstateAgentAuthenticationService;

@Service("estateAgentAuthenticationServiceImpl")
public class EstateAgentAuthenticationServiceImpl extends AuthenticationServiceImpl implements EstateAgentAuthenticationService {

	private final EstateAgentRepository estateAgentRepository;
	private final EstateAgentFactory estateAgentFactory;
	private final AdminFinder adminFinder;
	private final EstateAgentFinder estateAgentFinder;
	private final EstateAgentMapper estateAgentMapper;

	public EstateAgentAuthenticationServiceImpl(
			AccountFactory defaultAccountFactory,
			SecurityAccountDecoratorFactory secutiryAccountDecoratorFactory,
			DefaultAccountRepository defaultAccountRepository,
			RoleFinder roleFinder,
			UserRepository userRepository,
			UserMapper userMapper,
			UserFactory authenticationFactory,
			PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, 
			JwtService jwtService,
			EstateAgentRepository estateAgentRepository,
			EstateAgentFactory estateAgentFactory,
			AdminFinder adminFinder,
			EstateAgentFinder estateAgentFinder,
			EstateAgentMapper estateAgentMapper
			) {
		super(defaultAccountFactory, secutiryAccountDecoratorFactory, defaultAccountRepository, roleFinder, userRepository, userMapper, authenticationFactory, passwordEncoder, authenticationManager, jwtService);
		this.estateAgentRepository = estateAgentRepository;
		this.estateAgentFactory = estateAgentFactory;
		this.adminFinder = adminFinder;
		this.estateAgentFinder = estateAgentFinder;
		this.estateAgentMapper = estateAgentMapper;
	}

	@Override
	public void register(StafferRequest request) throws RoleNotFoundException {
		
		var stafferSpec = estateAgentMapper.toSpec(request);
		var estateAgentRole = roleFinder.getByRoleName("ROLE_ESTATE_AGENT");
		var adminCreator = adminFinder.getAdminByEmail(stafferSpec.getAdminEmail());
		
		var defaultAccount = defaultAccountFactory.createAccountFromSpec(stafferSpec, passwordEncoder, estateAgentRole);
		var securityAccountDecorator = secutiryAccountDecoratorFactory.createSecurityAccountDecoratorFromSpec(defaultAccount);
				
		var estateAgent = estateAgentFactory.createEstateAgentFromSpec(defaultAccount, adminCreator);
		defaultAccountRepository.save(defaultAccount);
		estateAgentRepository.save(estateAgent);
	}

}
