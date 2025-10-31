package com.dietiestates.resource_server.mapper;

import com.dietiestates.resource_server.dto.request.CadastralDataRequest;
import com.dietiestates.resource_server.dto.response.CadastralDataResponse;
import com.dietiestates.resource_server.model.CadastralData;
import com.dietiestates.resource_server.spec.CadastralDataSpec;

public interface CadastralDataMapper {

	CadastralDataSpec toSpec(CadastralDataRequest request);
	
	CadastralDataResponse fromEntity(CadastralData cadastralData);
}
