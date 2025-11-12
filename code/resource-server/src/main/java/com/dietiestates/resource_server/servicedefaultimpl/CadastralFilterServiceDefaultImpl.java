package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.CadastralFilterRequest;
import com.dietiestates.resource_server.dto.response.CadastralFilterResponse;
import com.dietiestates.resource_server.factory.CadastralFilterFactory;
import com.dietiestates.resource_server.finder.CadastralFilterFinder;
import com.dietiestates.resource_server.mapper.CadastralFilterMapper;
import com.dietiestates.resource_server.repository.CadastralFilterRepository;
import com.dietiestates.resource_server.service.CadastralFilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastralFilterServiceDefaultImpl implements CadastralFilterService {

	private final CadastralFilterRepository cadastralFilterRepository;
	private final CadastralFilterFactory cadastralFilterFactory;
	private final CadastralFilterFinder cadastralFilterFinder;
	//private final CadastralFilterVerifier cadastralFilterVerifier;
	private final CadastralFilterMapper cadastralFilterMapper;
	
	@Override
	public CadastralFilterResponse createCadastralFilter(CadastralFilterRequest request) {
		
		var cadastralFilterSpec = cadastralFilterMapper.toSpec(request);
		
		var cadastralFilter = cadastralFilterFactory.createCadastralFilterFromSpec(cadastralFilterSpec);
		cadastralFilterRepository.save(cadastralFilter);
		
		return cadastralFilterMapper.fromEntity(cadastralFilter);
	}
	
	@Override
	public CadastralFilterResponse getCadastralFilterById(Long cadastralFilterId) {
		var cadastralFilter = cadastralFilterFinder.getCadastralFilterById(cadastralFilterId);
		return cadastralFilterMapper.fromEntity(cadastralFilter);
	}

    @Override
    public CadastralFilterResponse getSearchCadastralFilter(Long searchId) {
        var cadastralFilter = cadastralFilterFinder.getSearchCadastralFilter(searchId);
        return cadastralFilterMapper.fromEntity(cadastralFilter);
    }
}
