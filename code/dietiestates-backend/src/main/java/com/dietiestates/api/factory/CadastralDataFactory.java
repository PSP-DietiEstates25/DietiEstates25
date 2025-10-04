package com.dietiestates.api.factory;

import com.dietiestates.api.dto.request.CadastralDataRequest;
import com.dietiestates.api.model.CadastralData;

public interface CadastralDataFactory {
	
	CadastralData createCadastralData(CadastralDataRequest request);
}
