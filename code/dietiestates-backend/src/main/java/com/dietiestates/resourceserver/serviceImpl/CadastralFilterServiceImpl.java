package com.dietiestates.resourceserver.serviceImpl;

import org.springframework.stereotype.Service;

import com.dietiestates.resourceserver.dto.request.CadastralFilterRequest;
import com.dietiestates.resourceserver.dto.response.CadastralFilterResponse;
import com.dietiestates.resourceserver.factory.CadastralFilterFactory;
import com.dietiestates.resourceserver.finder.CadastralFilterFinder;
import com.dietiestates.resourceserver.finder.SearchFinder;
import com.dietiestates.resourceserver.mapper.CadastralFilterMapper;
import com.dietiestates.resourceserver.repository.CadastralFilterRepository;
import com.dietiestates.resourceserver.service.CadastralFilterService;
import com.dietiestates.resourceserver.verifier.CadastralFilterVerifier;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CadastralFilterServiceImpl implements CadastralFilterService {

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
	
}
