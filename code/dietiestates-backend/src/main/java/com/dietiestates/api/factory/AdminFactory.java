package com.dietiestates.api.factory;

import com.dietiestates.api.model.Admin;
import com.dietiestates.api.model.DefaultAccount;

public interface AdminFactory {

	Admin createAdminFromSpec(
			DefaultAccount securityAccountDecorator,
			Admin admin
			);
}
