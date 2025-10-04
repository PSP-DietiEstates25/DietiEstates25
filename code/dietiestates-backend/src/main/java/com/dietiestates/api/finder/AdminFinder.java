package com.dietiestates.api.finder;

import com.dietiestates.api.exception.notfound.AdminNotFoundException;
import com.dietiestates.api.model.Admin;

public interface AdminFinder {

	Admin getAdminByEmail(String adminEmail)
			throws AdminNotFoundException;
}
