package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.StafferRequest;
import com.dietiestates.resource_server.dto.response.CreatedStaffersResponse;
import com.dietiestates.resource_server.dto.response.StafferResponse;
import com.dietiestates.resource_server.exception.notfound.AdminNotFoundException;
import com.dietiestates.resource_server.exception.notfound.RoleNotFoundException;
import com.dietiestates.resource_server.factory.AdminFactory;
import com.dietiestates.resource_server.finder.AdminFinder;
import com.dietiestates.resource_server.finder.EstateAgentFinder;
import com.dietiestates.resource_server.mapper.StafferMapper;
import com.dietiestates.resource_server.model.Admin;
import com.dietiestates.resource_server.model.EstateAgent;
import com.dietiestates.resource_server.repository.AdminRepository;
import com.dietiestates.resource_server.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceDefaultImpl implements AdminService {
	
	private final AdminRepository adminRepository;
	private final AdminFactory adminFactory;
	private final AdminFinder adminFinder;
	private final StafferMapper stafferMapper;

    private final EstateAgentFinder estateAgentFinder;

    @Override
	public StafferResponse register(StafferRequest request, String creatorEmail) throws RoleNotFoundException {
		
		var adminSpec = stafferMapper.toSpec(request);

		var adminCreator = adminFinder.getAdminByEmail(creatorEmail);
		var admin = adminFactory.createAdminFromSpec(adminSpec.getEmail(), adminCreator);

		adminRepository.save(admin);
        return stafferMapper.fromEntity(admin);
	}

    @Override
    public StafferResponse getAdminById(Long adminId) throws AdminNotFoundException {
        var admin = adminFinder.getAdminById(adminId);
        return stafferMapper.fromEntity(admin);
    }

    @Override
    public CreatedStaffersResponse getCreatedStaffers(String adminEmail, Integer page, Integer size) {

        String createdDate = "createdDate";

        Pageable pageable = PageRequest.of(page, size, Sort.by(createdDate).descending());
        var admin =  adminFinder.getAdminByEmail(adminEmail);

        var createdAdmins = this.getCreatedAdmins(admin, pageable);
        var createdEstateAgents = this.getCreatedEstateAgents(admin, pageable);

        return stafferMapper.fromStaffers(createdAdmins, createdEstateAgents);
    }

    private Page<Admin> getCreatedAdmins(Admin admin, Pageable pageable) {
        return adminFinder.getCreatedAdmins(admin, pageable);
    }

    private Page<EstateAgent> getCreatedEstateAgents(Admin admin, Pageable pageable) {
        return estateAgentFinder.getCreatedEstateAgents(admin, pageable);
    }
}

