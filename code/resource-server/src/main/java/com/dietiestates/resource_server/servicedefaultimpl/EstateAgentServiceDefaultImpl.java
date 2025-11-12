package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.StafferRequest;
import com.dietiestates.resource_server.dto.response.StafferResponse;
import com.dietiestates.resource_server.exception.notfound.EstateAgentNotFoundException;
import com.dietiestates.resource_server.exception.notfound.RoleNotFoundException;
import com.dietiestates.resource_server.factory.EstateAgentFactory;
import com.dietiestates.resource_server.finder.AdminFinder;
import com.dietiestates.resource_server.finder.EstateAgentFinder;
import com.dietiestates.resource_server.mapper.StafferMapper;
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
	private final StafferMapper stafferMapper;

	@Override
	public StafferResponse register(StafferRequest request, String creatorEmail) throws RoleNotFoundException {
		
		var stafferSpec = stafferMapper.toSpec(request);

		var adminCreator = adminFinder.getAdminByEmail(creatorEmail);
		var estateAgent = estateAgentFactory.createEstateAgentFromSpec(stafferSpec.getEmail(), adminCreator);

		estateAgentRepository.save(estateAgent);
        return stafferMapper.fromEntity(estateAgent);
	}

    @Override
    public StafferResponse getEstateAgentById(Long estateAgentId) throws EstateAgentNotFoundException {
        var estateAgent = estateAgentFinder.getEstateAgentById(estateAgentId);
        return stafferMapper.fromEntity(estateAgent);
    }

}
