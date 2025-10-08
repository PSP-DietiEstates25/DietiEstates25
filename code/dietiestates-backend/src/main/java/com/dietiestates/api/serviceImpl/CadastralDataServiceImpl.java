package com.dietiestates.api.serviceImpl;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.CadastralDataRequest;
import com.dietiestates.api.dto.response.CadastralDataResponse;
import com.dietiestates.api.factory.CadastralDataFactory;
import com.dietiestates.api.finder.CadastralDataFinder;
import com.dietiestates.api.mapper.CadastralDataMapper;
import com.dietiestates.api.repository.CadastralDataRepository;
import com.dietiestates.api.service.CadastralDataService;
import com.dietiestates.api.verifier.CadastralDataVerifier;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CadastralDataServiceImpl implements CadastralDataService {

	private final CadastralDataRepository cadastralDataRepository;
	private final CadastralDataFactory cadastralDataFactory;
	private final CadastralDataFinder cadastralDataFinder;
	//private final CadastralDataVerifier cadastralDataVerifier;
	private final CadastralDataMapper cadastralDataMapper;
	
	@Override
	public void createCadastralData(CadastralDataRequest request) {
		
		var cadastralDataSpec = cadastralDataMapper.toSpec(request);
		
		var cadastralData = cadastralDataFactory.createCadastralDataFromSpec(cadastralDataSpec);
		cadastralDataRepository.save(cadastralData);
	}
	
	@Override
	public CadastralDataResponse getCadastralDataById(Long cadastralDataId) {
		
		var cadastralData = cadastralDataFinder.getCadastralDataById(cadastralDataId);
		
		return cadastralDataMapper.fromEntity(cadastralData);
	}
}
