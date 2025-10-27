package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.CadastralFilterRequest;
import com.dietiestates.resource_server.dto.response.CadastralFilterResponse;

public interface CadastralFilterService {

	CadastralFilterResponse createCadastralFilter(CadastralFilterRequest request);
	
	CadastralFilterResponse getCadastralFilterById(Long cadastralFilterId);
}
