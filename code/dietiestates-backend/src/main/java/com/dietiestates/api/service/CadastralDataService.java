package com.dietiestates.api.service;

import com.dietiestates.api.dto.request.CadastralDataRequest;
import com.dietiestates.api.dto.response.CadastralDataResponse;

public interface CadastralDataService {

	void createCadastralData(CadastralDataRequest request, Long realEstateId);
	
	CadastralDataResponse getCadastralDataById(Long cadastralDataId, Long realEstateId);
}
