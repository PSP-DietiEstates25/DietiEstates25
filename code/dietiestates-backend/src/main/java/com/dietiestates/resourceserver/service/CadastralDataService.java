package com.dietiestates.resourceserver.service;

import com.dietiestates.resourceserver.dto.request.CadastralDataRequest;
import com.dietiestates.resourceserver.dto.response.CadastralDataResponse;

public interface CadastralDataService {

	CadastralDataResponse createCadastralData(CadastralDataRequest request);
	
	CadastralDataResponse getCadastralDataById(Long cadastralDataId);
}
