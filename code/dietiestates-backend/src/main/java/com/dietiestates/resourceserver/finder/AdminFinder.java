package com.dietiestates.resourceserver.finder;

import java.util.List;

import com.dietiestates.resourceserver.exception.notfound.AdminNotFoundException;
import com.dietiestates.resourceserver.model.Admin;

public interface AdminFinder {

	Admin getAdminByEmail(String adminEmail)
			throws AdminNotFoundException;
	
	List<Admin> getAllAdmins();
}
