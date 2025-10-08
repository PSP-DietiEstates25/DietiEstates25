package com.dietiestates.api.serviceImpl;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.CadastralFilterRequest;
import com.dietiestates.api.dto.response.CadastralFilterResponse;
import com.dietiestates.api.factory.CadastralFilterFactory;
import com.dietiestates.api.finder.CadastralFilterFinder;
import com.dietiestates.api.finder.SearchFinder;
import com.dietiestates.api.mapper.CadastralFilterMapper;
import com.dietiestates.api.repository.CadastralFilterRepository;
import com.dietiestates.api.service.CadastralFilterService;
import com.dietiestates.api.verifier.CadastralFilterVerifier;

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
	public void createCadastralFilter(CadastralFilterRequest request) {
		
		var cadastralFilterSpec = cadastralFilterMapper.toSpec(request);
		
		var cadastralFilter = cadastralFilterFactory.createCadastralFilterFromSpec(cadastralFilterSpec);
		cadastralFilterRepository.save(cadastralFilter);
	}
	
	@Override
	public CadastralFilterResponse getCadastralFilterById(Long cadastralFilterId) {
		
		var cadastralFilter = cadastralFilterFinder.getCadastralFilterById(cadastralFilterId);

		return cadastralFilterMapper.fromEntity(cadastralFilter);
	}
	
}
