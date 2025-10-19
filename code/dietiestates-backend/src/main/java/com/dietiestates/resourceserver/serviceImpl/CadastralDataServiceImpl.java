package com.dietiestates.resourceserver.serviceImpl;

import org.springframework.stereotype.Service;

import com.dietiestates.resourceserver.dto.request.CadastralDataRequest;
import com.dietiestates.resourceserver.dto.response.CadastralDataResponse;
import com.dietiestates.resourceserver.factory.CadastralDataFactory;
import com.dietiestates.resourceserver.finder.CadastralDataFinder;
import com.dietiestates.resourceserver.mapper.CadastralDataMapper;
import com.dietiestates.resourceserver.repository.CadastralDataRepository;
import com.dietiestates.resourceserver.service.CadastralDataService;
import com.dietiestates.resourceserver.verifier.CadastralDataVerifier;

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
	public CadastralDataResponse createCadastralData(CadastralDataRequest request) {
		
		var cadastralDataSpec = cadastralDataMapper.toSpec(request);
		
		var cadastralData = cadastralDataFactory.createCadastralDataFromSpec(cadastralDataSpec);
		cadastralDataRepository.save(cadastralData);
		
		return cadastralDataMapper.fromEntity(cadastralData);
	}
	
	@Override
	public CadastralDataResponse getCadastralDataById(Long cadastralDataId) {
		
		var cadastralData = cadastralDataFinder.getCadastralDataById(cadastralDataId);
		
		return cadastralDataMapper.fromEntity(cadastralData);
	}
}
