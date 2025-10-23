package com.dietiestates.resourceserver.mapper;

import com.dietiestates.resourceserver.dto.request.CadastralDataRequest;
import com.dietiestates.resourceserver.dto.response.CadastralDataResponse;
import com.dietiestates.resourceserver.model.CadastralData;
import com.dietiestates.resourceserver.spec.CadastralDataSpec;

public interface CadastralDataMapper {

	CadastralDataSpec toSpec(CadastralDataRequest request);
	
	CadastralDataResponse fromEntity(CadastralData cadastralData);
}
