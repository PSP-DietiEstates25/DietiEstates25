package com.dietiestates.resource_server.factory;

import com.dietiestates.resource_server.model.Admin;
import com.dietiestates.resource_server.model.EstateAgent;

public interface EstateAgentFactory {

    EstateAgent createEstateAgentFromSpec(
            String email,
            Admin admin
    );
}
