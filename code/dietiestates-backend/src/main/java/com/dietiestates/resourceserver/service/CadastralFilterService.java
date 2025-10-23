package com.dietiestates.resourceserver.service;

import com.dietiestates.resourceserver.dto.request.CadastralFilterRequest;
import com.dietiestates.resourceserver.dto.response.CadastralFilterResponse;

public interface CadastralFilterService {

	CadastralFilterResponse createCadastralFilter(CadastralFilterRequest request);
	
	CadastralFilterResponse getCadastralFilterById(Long cadastralFilterId);
}
