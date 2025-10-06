package com.dietiestates.api.service;

import com.dietiestates.api.dto.request.CadastralFilterRequest;
import com.dietiestates.api.dto.response.CadastralFilterResponse;

public interface CadastralFilterService {

	void createCadastralFilter(CadastralFilterRequest request, Long searchId);
	
	CadastralFilterResponse getCadastralFilterById(Long searchId, Long cadastralFilterId);
}
