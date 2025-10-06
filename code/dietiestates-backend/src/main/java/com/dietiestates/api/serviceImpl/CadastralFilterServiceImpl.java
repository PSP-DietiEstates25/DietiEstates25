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
	private final CadastralFilterVerifier cadastralFilterVerifier;
	private final CadastralFilterMapper cadastralFilterMapper;
	
	private final SearchFinder searchFinder;
	
	@Override
	public void createCadastralFilter(CadastralFilterRequest request, Long searchId) {
		
		var cadastralFilterSpec = cadastralFilterMapper.toSpec(request);
		
		var search = searchFinder.getSearchById(searchId);
		
		var cadastralFilter = cadastralFilterFactory.createCadastralFilterFromSpec(cadastralFilterSpec, search);
		cadastralFilterRepository.save(cadastralFilter);
	}
	
	@Override
	public CadastralFilterResponse getCadastralFilterById(Long searchId, Long cadastralFilterId) {
		
		var cadastralFilter = cadastralFilterFinder.getCadastralFilterById(cadastralFilterId);
		var search = searchFinder.getSearchById(searchId);
		
		cadastralFilterVerifier.checkCadastralFilterOwnedBySearch(cadastralFilter.getSearch().getId(), search.getId());

		return cadastralFilterMapper.fromEntity(cadastralFilter);
	}
	
}
