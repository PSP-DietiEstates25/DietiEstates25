package com.dietiestates.resourceserver.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.factory.AdminFactory;
import com.dietiestates.resourceserver.model.Admin;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminFactoryImpl implements AdminFactory {
	
	@Override
	public Admin createAdminFromSpec(
			String email,
			Admin admin
			) {
		return Admin.builder()
				.email(email)
				.admin(admin)
				.build();
	}
}
