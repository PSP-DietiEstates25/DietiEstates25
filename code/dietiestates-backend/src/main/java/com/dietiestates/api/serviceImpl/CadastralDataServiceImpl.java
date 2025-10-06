package com.dietiestates.api.serviceImpl;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.CadastralDataRequest;
import com.dietiestates.api.dto.response.CadastralDataResponse;
import com.dietiestates.api.factory.CadastralDataFactory;
import com.dietiestates.api.finder.CadastralDataFinder;
import com.dietiestates.api.finder.RealEstateFinder;
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
	private final CadastralDataVerifier cadastralDataVerifier;
	private final CadastralDataMapper cadastralDataMapper;
	
	private final RealEstateFinder realEstateFinder;
	
	@Override
	public void createCadastralData(CadastralDataRequest request, Long realEstateId) {
		
		var cadastralDataSpec = cadastralDataMapper.toSpec(request);
		
		var realEstate = realEstateFinder.getRealEstateById(realEstateId);
		
		var cadastralData = cadastralDataFactory.createCadastralDataFromSpec(cadastralDataSpec, realEstate);
		cadastralDataRepository.save(cadastralData);
	}
	
	@Override
	public CadastralDataResponse getCadastralDataById(Long cadastralDataId, Long realEstateId) {
		
		var cadastralData = cadastralDataFinder.getCadastralDataById(cadastralDataId);
		var realEstate = realEstateFinder.getRealEstateById(realEstateId);
	
		cadastralDataVerifier.checkCadastralDataOwnedByRealEstate(
				cadastralData.getRealEstate().getId(),
				realEstate.getId()
				);
		
		return cadastralDataMapper.fromEntity(cadastralData);
	}
}
