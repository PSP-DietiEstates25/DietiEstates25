package com.dietiestates.api.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.exception.notfound.AdminNotFoundException;
import com.dietiestates.api.finder.AdminFinder;
import com.dietiestates.api.model.Admin;
import com.dietiestates.api.repository.AdminRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminFinderImpl implements AdminFinder {

	private final AdminRepository adminRepository;
	
	public Admin getAdminByEmail(String adminEmail) throws AdminNotFoundException {
		return adminRepository.findByEmail(adminEmail)
				.orElseThrow(AdminNotFoundException::new);
	}
}
