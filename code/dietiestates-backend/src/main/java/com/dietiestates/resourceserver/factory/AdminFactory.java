package com.dietiestates.resourceserver.factory;

import com.dietiestates.resourceserver.model.Admin;

public interface AdminFactory {

	Admin createAdminFromSpec(
			String email,
			Admin admin
			);
}
