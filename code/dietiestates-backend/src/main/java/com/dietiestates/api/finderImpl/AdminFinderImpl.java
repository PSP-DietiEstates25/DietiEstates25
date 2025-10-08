package com.dietiestates.api.finderImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.dietiestates.api.exception.notfound.AdminNotFoundException;
import com.dietiestates.api.finder.AdminFinder;
import com.dietiestates.api.finder.DefaultAccountFinder;
import com.dietiestates.api.model.Admin;
import com.dietiestates.api.repository.AdminRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminFinderImpl implements AdminFinder {

	private final AdminRepository adminRepository;
	private final DefaultAccountFinder defaultAccountFinder;
	
	public Admin getAdminByEmail(String adminEmail)
			throws AdminNotFoundException {
		
		var account = defaultAccountFinder.getDefaultAccountByEmail(adminEmail);
		var allAdmins = getAllAdmins();
		
		for(Admin admin: allAdmins) {
			if(admin.getSecurityAccountDecorator().getAccountEmail().equals(account.getEmail())){
				return admin;
			}
		}
		
		throw new AdminNotFoundException();
	}
	
	@Override
	public List<Admin> getAllAdmins() {
		
		var adminsIterable = adminRepository.findAll();
		var allAdmins = new ArrayList<Admin>();
		adminsIterable.forEach(allAdmins::add);
		
		return allAdmins;
	}
}
