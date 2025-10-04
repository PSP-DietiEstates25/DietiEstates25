package com.dietiestates.api.mapper;

import com.dietiestates.api.dto.request.CadastralFilterRequest;
import com.dietiestates.api.dto.response.CadastralFilterResponse;
import com.dietiestates.api.model.CadastralFilter;
import com.dietiestates.api.spec.CadastralFilterSpec;

public interface CadastralFilterMapper {

	public CadastralFilterSpec toSpec(CadastralFilterRequest request);
	
	public CadastralFilterResponse fromEntity(CadastralFilter cadastralFilter);
}
