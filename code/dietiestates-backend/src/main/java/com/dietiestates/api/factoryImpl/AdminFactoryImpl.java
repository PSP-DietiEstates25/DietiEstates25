package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.factory.AdminFactory;
import com.dietiestates.api.model.Admin;
import com.dietiestates.api.model.DefaultAccount;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminFactoryImpl implements AdminFactory {
	
	@Override
	public Admin createAdminFromSpec(
			DefaultAccount securityAccountDecorator,
			Admin admin
			) {
		return Admin.builder()
				.securityAccountDecorator(securityAccountDecorator)
				.admin(admin)
				.build();
	}
}
