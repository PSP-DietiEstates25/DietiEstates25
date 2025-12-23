package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.AdminNotFoundException;
import com.dietiestates.resource_server.model.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminFinder {
	Admin getAdminByEmail(String adminEmail) throws AdminNotFoundException;
    Admin getAdminById(Long id) throws AdminNotFoundException;
    Page<Admin> getCreatedAdmins(Admin admin, Pageable pageable);
}
