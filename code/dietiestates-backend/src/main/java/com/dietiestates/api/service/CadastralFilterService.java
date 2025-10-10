package com.dietiestates.api.service;

import com.dietiestates.api.dto.request.CadastralFilterRequest;
import com.dietiestates.api.dto.response.CadastralFilterResponse;

public interface CadastralFilterService {

	CadastralFilterResponse createCadastralFilter(CadastralFilterRequest request);
	
	CadastralFilterResponse getCadastralFilterById(Long cadastralFilterId);
}
