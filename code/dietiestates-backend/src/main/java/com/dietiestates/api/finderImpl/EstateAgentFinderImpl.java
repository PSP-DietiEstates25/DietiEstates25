package com.dietiestates.api.finderImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.dietiestates.api.exception.notfound.EstateAgentNotFoundException;
import com.dietiestates.api.finder.DefaultAccountFinder;
import com.dietiestates.api.finder.EstateAgentFinder;
import com.dietiestates.api.model.EstateAgent;
import com.dietiestates.api.repository.EstateAgentRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EstateAgentFinderImpl implements EstateAgentFinder {

	private final EstateAgentRepository estateAgentRepository;
	private final DefaultAccountFinder defaultAccountFinder;
	
	@Override
	public EstateAgent getEstateAgentByEmail(String agentEmail)
			throws EstateAgentNotFoundException {
		
		var account = defaultAccountFinder.getDefaultAccountByEmail(agentEmail);
		var allEstateAgents = getAllEstateAgents();
		
		for(EstateAgent estateAgent: allEstateAgents) {
			if(estateAgent.getSecurityAccountDecorator().getAccountEmail().equals(account.getEmail())){
				return estateAgent;
			}
		}
		
		throw new EstateAgentNotFoundException();
	}

	@Override
	public List<EstateAgent> getAllEstateAgents() {
		
		var estateAgentsIterable = estateAgentRepository.findAll();
		var allEstateAgents = new ArrayList<EstateAgent>();
		estateAgentsIterable.forEach(allEstateAgents::add);
		
		return allEstateAgents;
	}
}
