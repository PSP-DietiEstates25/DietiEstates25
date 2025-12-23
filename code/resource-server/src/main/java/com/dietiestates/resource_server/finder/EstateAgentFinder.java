package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.EstateAgentNotFoundException;
import com.dietiestates.resource_server.model.Admin;
import com.dietiestates.resource_server.model.EstateAgent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EstateAgentFinder {
	EstateAgent getEstateAgentByEmail(String agentEmail) throws EstateAgentNotFoundException;
    EstateAgent getEstateAgentById(Long estateAgentId) throws EstateAgentNotFoundException;
    Page<EstateAgent> getCreatedEstateAgents(Admin admin, Pageable pageable);
}
