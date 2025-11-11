package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.CadastralDataRequest;
import com.dietiestates.resource_server.dto.response.CadastralDataResponse;

public interface CadastralDataService {
	CadastralDataResponse createCadastralData(CadastralDataRequest request);
	CadastralDataResponse getCadastralDataById(Long cadastralDataId);
    CadastralDataResponse getRealEstateCadastralData(Long realEstateId);
    void updateCadastralData(Long cadastralDataId, CadastralDataRequest request);
}
