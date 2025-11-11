package com.dietiestates.resource_server.factory;

import com.dietiestates.resource_server.model.Admin;

public interface AdminFactory {
    Admin createAdminFromSpec(String email, Admin admin);
}
