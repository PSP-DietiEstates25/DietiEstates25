package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.AdminNotFoundException;
import com.dietiestates.resource_server.finder.AdminFinder;
import com.dietiestates.resource_server.model.Admin;
import com.dietiestates.resource_server.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminFinderDefaultImpl implements AdminFinder {

	private final AdminRepository adminRepository;

    @Override
	public Admin getAdminByEmail(String adminEmail)
			throws AdminNotFoundException {
		
		return adminRepository.findByEmail(adminEmail)
				.orElseThrow(AdminNotFoundException::new);
	}

    @Override
    public Admin getAdminById(Long id) throws AdminNotFoundException {
        return adminRepository.findById(id)
                .orElseThrow(AdminNotFoundException::new);
    }

    @Override
    public Page<Admin> getCreatedAdmins(Admin admin, Pageable pageable) {
        return adminRepository.findByAdminId(admin.getId(), pageable);
    }
}
