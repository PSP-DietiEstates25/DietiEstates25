package com.dietiestates.api.factory;

import com.dietiestates.api.model.CadastralData;
import com.dietiestates.api.spec.CadastralDataSpec;

public interface CadastralDataFactory {
	
	CadastralData createCadastralDataFromSpec(
			CadastralDataSpec spec
			);
}
