package com.dietiestates.resourceserver.finderImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.exception.notfound.EstateAgentNotFoundException;
import com.dietiestates.resourceserver.finder.EstateAgentFinder;
import com.dietiestates.resourceserver.model.EstateAgent;
import com.dietiestates.resourceserver.repository.EstateAgentRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EstateAgentFinderImpl implements EstateAgentFinder {

	private final EstateAgentRepository estateAgentRepository;
	
	@Override
	public EstateAgent getEstateAgentByEmail(String agentEmail)
			throws EstateAgentNotFoundException {
		
		return estateAgentRepository.findByEmail(agentEmail)
				.orElseThrow(EstateAgentNotFoundException::new);
	}

	@Override
	public List<EstateAgent> getAllEstateAgents() {
		
		var estateAgentsIterable = estateAgentRepository.findAll();
		var allEstateAgents = new ArrayList<EstateAgent>();
		estateAgentsIterable.forEach(allEstateAgents::add);
		
		return allEstateAgents;
	}
}
