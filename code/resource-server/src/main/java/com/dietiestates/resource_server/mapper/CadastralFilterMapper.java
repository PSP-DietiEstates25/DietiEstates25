package com.dietiestates.resource_server.mapper;

import com.dietiestates.resource_server.dto.request.CadastralFilterRequest;
import com.dietiestates.resource_server.dto.response.CadastralFilterResponse;
import com.dietiestates.resource_server.model.CadastralFilter;
import com.dietiestates.resource_server.spec.CadastralFilterSpec;

public interface CadastralFilterMapper {

	public CadastralFilterSpec toSpec(CadastralFilterRequest request);
	
	public CadastralFilterResponse fromEntity(CadastralFilter cadastralFilter);
}
