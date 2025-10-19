package com.dietiestates.resourceserver.finderImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.exception.notfound.AdminNotFoundException;
import com.dietiestates.resourceserver.finder.AdminFinder;
import com.dietiestates.resourceserver.finder.DefaultAccountFinder;
import com.dietiestates.resourceserver.model.Admin;
import com.dietiestates.resourceserver.repository.AdminRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminFinderImpl implements AdminFinder {

	private final AdminRepository adminRepository;
	
	public Admin getAdminByEmail(String adminEmail)
			throws AdminNotFoundException {
		
		return adminRepository.findByEmail(adminEmail)
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
