package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.EstateAgentNotFoundException;
import com.dietiestates.resource_server.finder.EstateAgentFinder;
import com.dietiestates.resource_server.model.Admin;
import com.dietiestates.resource_server.model.EstateAgent;
import com.dietiestates.resource_server.repository.EstateAgentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EstateAgentFinderDefaultImpl implements EstateAgentFinder {

	private final EstateAgentRepository estateAgentRepository;
	
	@Override
	public EstateAgent getEstateAgentByEmail(String agentEmail)
			throws EstateAgentNotFoundException {
		
		return estateAgentRepository.findByEmail(agentEmail)
				.orElseThrow(EstateAgentNotFoundException::new);
	}

    @Override
    public EstateAgent getEstateAgentById(Long estateAgentId) throws EstateAgentNotFoundException {
        return estateAgentRepository.findById(estateAgentId)
                .orElseThrow(EstateAgentNotFoundException::new);
    }

    @Override
    public Page<EstateAgent> getCreatedEstateAgents(Admin admin, Pageable pageable) {
        return estateAgentRepository.findByAdminId(admin.getId(), pageable);
    }
}
