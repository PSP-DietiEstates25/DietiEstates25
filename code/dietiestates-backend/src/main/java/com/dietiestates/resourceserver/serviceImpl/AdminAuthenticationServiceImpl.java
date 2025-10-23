package com.dietiestates.resourceserver.serviceImpl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dietiestates.resourceserver.dto.request.StafferRequest;
import com.dietiestates.resourceserver.exception.notfound.RoleNotFoundException;
import com.dietiestates.resourceserver.factory.AccountFactory;
import com.dietiestates.resourceserver.factory.AdminFactory;
import com.dietiestates.resourceserver.factory.SecurityAccountDecoratorFactory;
import com.dietiestates.resourceserver.factory.UserFactory;
import com.dietiestates.resourceserver.finder.AdminFinder;
import com.dietiestates.resourceserver.finder.EstateAgentFinder;
import com.dietiestates.resourceserver.finder.RoleFinder;
import com.dietiestates.resourceserver.finder.UserFinder;
import com.dietiestates.resourceserver.mapper.AdminMapper;
import com.dietiestates.resourceserver.mapper.UserMapper;
import com.dietiestates.resourceserver.repository.AdminRepository;
import com.dietiestates.resourceserver.repository.DefaultAccountRepository;
import com.dietiestates.resourceserver.repository.UserRepository;
import com.dietiestates.resourceserver.service.AdminAuthenticationService;

@Service("adminAuthenticationServiceImpl")
public class AdminAuthenticationServiceImpl extends AuthenticationServiceImpl implements AdminAuthenticationService {
	
	private final AdminRepository adminRepository;
	private final AdminFactory adminFactory;
	private final AdminFinder adminFinder;
	private final AdminMapper adminMapper;

	public AdminAuthenticationServiceImpl(
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
			AdminRepository adminRepository,
			UserFinder userFinder,
			EstateAgentFinder estateAgentFinder,
			AdminFinder adminFinder,
			AdminMapper adminMapper,
			AdminFactory adminFactory
			) {
		super(defaultAccountFactory, secutiryAccountDecoratorFactory, defaultAccountRepository, roleFinder, userRepository, userMapper, authenticationFactory, passwordEncoder, authenticationManager, jwtService);
		this.adminRepository = adminRepository;
		this.adminFinder = adminFinder;
		this.adminMapper = adminMapper;
		this.adminFactory = adminFactory;
	}
	
	@Override
	public void register(StafferRequest request) throws RoleNotFoundException {
		
		var stafferSpec = adminMapper.toSpec(request);
		var adminRole = roleFinder.getByRoleName("ROLE_ADMIN");
		var adminCreator = adminFinder.getAdminByEmail(stafferSpec.getAdminEmail());
		
		var defaultAccount = defaultAccountFactory.createAccountFromSpec(stafferSpec, passwordEncoder, adminRole);
		var securityAccountDecorator = secutiryAccountDecoratorFactory.createSecurityAccountDecoratorFromSpec(defaultAccount);
		
		var admin = adminFactory.createAdminFromSpec(defaultAccount, adminCreator);
		defaultAccountRepository.save(defaultAccount);
		adminRepository.save(admin);
	}
}

