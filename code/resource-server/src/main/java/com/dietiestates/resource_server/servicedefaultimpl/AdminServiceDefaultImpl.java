package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.StafferRequest;
import com.dietiestates.resource_server.dto.response.AdminResponse;
import com.dietiestates.resource_server.exception.notfound.AdminNotFoundException;
import com.dietiestates.resource_server.exception.notfound.RoleNotFoundException;
import com.dietiestates.resource_server.factory.AdminFactory;
import com.dietiestates.resource_server.finder.AdminFinder;
import com.dietiestates.resource_server.finder.RoleFinder;
import com.dietiestates.resource_server.mapper.AdminMapper;
import com.dietiestates.resource_server.repository.AdminRepository;
import com.dietiestates.resource_server.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class AdminServiceDefaultImpl implements AdminService {
	
	private final AdminRepository adminRepository;
	private final AdminFactory adminFactory;
	private final AdminFinder adminFinder;
	private final AdminMapper adminMapper;

	private final RoleFinder roleFinder;

	@Override
	public AdminResponse register(StafferRequest request) throws RoleNotFoundException {
		
		var adminSpec = adminMapper.toSpec(request);
		var adminRole = roleFinder.getByRoleName("ROLE_ADMIN");
		var adminCreator = adminFinder.getAdminByEmail(adminSpec.getAdminEmail());
		
		var admin = adminFactory.createAdminFromSpec(adminSpec.getEmail(), adminCreator);
		adminRepository.save(admin);

        return adminMapper.fromEntity(admin);
	}

    @Override
    public AdminResponse getAdminById(Long adminId) throws AdminNotFoundException {
        var admin = adminFinder.getAdminById(adminId);
        return adminMapper.fromEntity(admin);
    }

}

