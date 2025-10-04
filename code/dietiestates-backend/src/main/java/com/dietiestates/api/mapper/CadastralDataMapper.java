package com.dietiestates.api.mapper;

import com.dietiestates.api.dto.request.CadastralDataRequest;
import com.dietiestates.api.dto.response.CadastralDataResponse;
import com.dietiestates.api.model.CadastralData;
import com.dietiestates.api.spec.CadastralDataSpec;

public interface CadastralDataMapper {

	CadastralDataSpec toSpec(CadastralDataRequest request);
	
	CadastralDataResponse fromEntity(CadastralData cadastralData);
}
