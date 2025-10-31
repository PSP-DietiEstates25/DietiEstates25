package com.dietiestates.resource_server.finder;

import java.util.List;

import com.dietiestates.resource_server.exception.notfound.AdminNotFoundException;
import com.dietiestates.resource_server.model.Admin;

public interface AdminFinder {

	Admin getAdminByEmail(String adminEmail)
			throws AdminNotFoundException;

    Admin getAdminById(Long id)
            throws AdminNotFoundException;

	List<Admin> getAllAdmins();
}
