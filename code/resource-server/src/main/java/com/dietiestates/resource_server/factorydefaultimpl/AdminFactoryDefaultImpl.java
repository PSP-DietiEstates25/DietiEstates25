package com.dietiestates.resource_server.factorydefaultimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resource_server.factory.AdminFactory;
import com.dietiestates.resource_server.model.Admin;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminFactoryDefaultImpl implements AdminFactory {

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
