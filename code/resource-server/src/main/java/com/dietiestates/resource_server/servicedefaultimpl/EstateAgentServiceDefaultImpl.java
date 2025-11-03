package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.StafferRequest;
import com.dietiestates.resource_server.dto.response.EstateAgentResponse;
import com.dietiestates.resource_server.exception.notfound.EstateAgentNotFoundException;
import com.dietiestates.resource_server.exception.notfound.RoleNotFoundException;
import com.dietiestates.resource_server.factory.EstateAgentFactory;
import com.dietiestates.resource_server.finder.AdminFinder;
import com.dietiestates.resource_server.finder.EstateAgentFinder;
import com.dietiestates.resource_server.finder.RoleFinder;
import com.dietiestates.resource_server.mapper.EstateAgentMapper;
import com.dietiestates.resource_server.repository.EstateAgentRepository;
import com.dietiestates.resource_server.service.EstateAgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstateAgentServiceDefaultImpl implements EstateAgentService {

	private final EstateAgentRepository estateAgentRepository;
	private final EstateAgentFactory estateAgentFactory;
	private final AdminFinder adminFinder;
	private final EstateAgentFinder estateAgentFinder;
	private final EstateAgentMapper estateAgentMapper;

    private final RoleFinder roleFinder;

	@Override
	public EstateAgentResponse register(StafferRequest request, String creatorEmail) throws RoleNotFoundException {
		
		var stafferSpec = estateAgentMapper.toSpec(request);

		var adminCreator = adminFinder.getAdminByEmail(creatorEmail);
		var estateAgent = estateAgentFactory.createEstateAgentFromSpec(stafferSpec.getEmail(), adminCreator);

		estateAgentRepository.save(estateAgent);
        return estateAgentMapper.fromEntity(estateAgent);
	}

    @Override
    public EstateAgentResponse getEstateAgentById(Long estateAgentId) throws EstateAgentNotFoundException {
        var estateAgent = estateAgentFinder.getEstateAgentById(estateAgentId);
        return estateAgentMapper.fromEntity(estateAgent);
    }

}
