package com.dietiestates.api.serviceImpl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.StafferRequest;
import com.dietiestates.api.exception.notfound.RoleNotFoundException;
import com.dietiestates.api.factory.AdminFactory;
import com.dietiestates.api.factory.AuthenticationFactory;
import com.dietiestates.api.finder.AdminFinder;
import com.dietiestates.api.finder.EstateAgentFinder;
import com.dietiestates.api.finder.RoleFinder;
import com.dietiestates.api.finder.UserFinder;
import com.dietiestates.api.mapper.AdminMapper;
import com.dietiestates.api.mapper.UserMapper;
import com.dietiestates.api.repository.AdminRepository;
import com.dietiestates.api.repository.UserRepository;
import com.dietiestates.api.security.JwtService;
import com.dietiestates.api.service.AdminAuthenticationService;

@Service("adminAuthenticationServiceImpl")
public class AdminAuthenticationServiceImpl extends AuthenticationServiceImpl implements AdminAuthenticationService {
	
	private final AdminRepository adminRepository;
	private final AdminFactory adminFactory;
	private final AdminFinder adminFinder;
	private final AdminMapper adminMapper;

	public AdminAuthenticationServiceImpl(
			RoleFinder roleFinder,
			UserRepository userRepository,
			UserMapper userMapper,
			AuthenticationFactory authenticationFactory,
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
		super(roleFinder, userRepository, userMapper, authenticationFactory, passwordEncoder, authenticationManager, jwtService);
		this.adminRepository = adminRepository;
		this.adminFinder = adminFinder;
		this.adminMapper = adminMapper;
		this.adminFactory = adminFactory;
	}
	
	@Override
	public void register(StafferRequest request) throws RoleNotFoundException {
		
		var stafferSpec = adminMapper.toSpec(request);
		var adminRole = roleFinder.getByRoleName("ADMIN");
		var creator = adminFinder.getAdminByEmail(stafferSpec.getAdminEmail());

		var admin = adminFactory.createAdminFromSpec(stafferSpec, passwordEncoder, adminRole, creator);
		adminRepository.save(admin);
	}
}

