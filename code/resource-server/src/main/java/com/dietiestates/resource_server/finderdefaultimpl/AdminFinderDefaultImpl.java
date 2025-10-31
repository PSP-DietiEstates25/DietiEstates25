package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.AdminNotFoundException;
import com.dietiestates.resource_server.finder.AdminFinder;
import com.dietiestates.resource_server.model.Admin;
import com.dietiestates.resource_server.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
	public List<Admin> getAllAdmins() {
		
		var adminsIterable = adminRepository.findAll();
		var allAdmins = new ArrayList<Admin>();
		adminsIterable.forEach(allAdmins::add);
		
		return allAdmins;
	}
}
