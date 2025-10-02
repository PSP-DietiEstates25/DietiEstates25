package com.dietiestates.api.service;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.CadastralDataRequest;
import com.dietiestates.api.dto.response.CadastralDataResponse;
import com.dietiestates.api.exception.notfound.CadastralDataNotFoundException;
import com.dietiestates.api.exception.notowned.CadastralDataNotOwnedByRealEstateException;
import com.dietiestates.api.mapper.CadastralDataMapper;
import com.dietiestates.api.model.CadastralData;
import com.dietiestates.api.repository.CadastralDataRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CadastralDataService {

	private final CadastralDataRepository cadastralDataRepository;	
	private final CadastralDataMapper cadastralDataMapper;
	
	private final RealEstateService realEstateService;
	
	public CadastralData createCadastralData(CadastralDataRequest request, Long realEstateId) {
		
		var realEstate = realEstateService.getRealEstateById(realEstateId);		
		var cadastralData = cadastralDataMapper.toEntity(request, realEstate);
		return cadastralDataRepository.save(cadastralData);
	}
	
	public CadastralDataResponse getCadastralData(Long cadastralDataId, Long realEstateId) {
		
		var cadastralData = this.getCadastralDataById(cadastralDataId);
		var realEstate = realEstateService.getRealEstateById(realEstateId);
	
		this.checkCadastralDataOwnedByRealEstate(cadastralData.getRealEstate().getId(), realEstate.getId());
		
		return cadastralDataMapper.fromEntity(cadastralData);
	}
	
	public CadastralData getCadastralDataById(Long id) {
		return cadastralDataRepository.findById(id)
				.orElseThrow(CadastralDataNotFoundException::new);
	}
	
	public void checkCadastralDataOwnedByRealEstate(Long cadastralDataRealEstateId, Long realEstateId) {
		
		 if(!cadastralDataRealEstateId.equals(realEstateId))
			 throw new CadastralDataNotOwnedByRealEstateException();
	}
}
