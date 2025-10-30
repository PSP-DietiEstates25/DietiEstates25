package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.UtilityRequest;
import com.dietiestates.resource_server.dto.response.UtilityResponse;
import com.dietiestates.resource_server.factory.UtilityFactory;
import com.dietiestates.resource_server.finder.UtilityFinder;
import com.dietiestates.resource_server.mapper.UtilityMapper;
import com.dietiestates.resource_server.repository.UtilityRepository;
import com.dietiestates.resource_server.service.UtilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UtilityServiceDefaultImpl implements UtilityService {

	private final UtilityRepository utilityRepository;
	private final UtilityFactory utilityFactory;
	private final UtilityFinder utilityFinder;
	//private final UtilityVerifier utilityVerifier;
	private final UtilityMapper utilityMapper;
	
	@Override
	public UtilityResponse createUtility(UtilityRequest request) {
		
		var utilitySpec = utilityMapper.toSpec(request);
		var utility = utilityFactory.createUtilityFromSpec(utilitySpec);

		utilityRepository.save(utility);
		return utilityMapper.fromEntity(utility);
	}
	
	@Override
	public UtilityResponse getUtilityById(Long utilityId) {
		
		var utility = utilityFinder.getUtilityById(utilityId);
		return utilityMapper.fromEntity(utility);
	}

    @Override
    @Transactional
    public UtilityResponse updateUtility(Long id, UtilityRequest request) {

        var utilitySpec = utilityMapper.toSpec(request);

        var utilityToUpdate = utilityFinder.getUtilityById(id);
        utilityToUpdate.setHasElevator(utilitySpec.getHasElevator());
        utilityToUpdate.setHasDoorman(utilitySpec.getHasDoorman());
        utilityToUpdate.setHasAirConditioning(utilitySpec.getHasAirConditioning());
        utilityToUpdate.setNearPark(utilitySpec.getNearPark());
        utilityToUpdate.setNearPublicTransport(utilitySpec.getNearPublicTransport());
        utilityToUpdate.setNearSchool(utilitySpec.getNearSchool());

        utilityRepository.save(utilityToUpdate);
        return utilityMapper.fromEntity(utilityToUpdate);
    }
}
