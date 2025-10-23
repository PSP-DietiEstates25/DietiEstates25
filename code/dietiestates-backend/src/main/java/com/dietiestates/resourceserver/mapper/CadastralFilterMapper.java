package com.dietiestates.resourceserver.mapper;

import com.dietiestates.resourceserver.dto.request.CadastralFilterRequest;
import com.dietiestates.resourceserver.dto.response.CadastralFilterResponse;
import com.dietiestates.resourceserver.model.CadastralFilter;
import com.dietiestates.resourceserver.spec.CadastralFilterSpec;

public interface CadastralFilterMapper {

	public CadastralFilterSpec toSpec(CadastralFilterRequest request);
	
	public CadastralFilterResponse fromEntity(CadastralFilter cadastralFilter);
}
