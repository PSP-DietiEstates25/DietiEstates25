package com.dietiestates.api.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.exception.notfound.EstateAgentNotFoundException;
import com.dietiestates.api.finder.EstateAgentFinder;
import com.dietiestates.api.model.EstateAgent;
import com.dietiestates.api.repository.EstateAgentRepository;

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

}
