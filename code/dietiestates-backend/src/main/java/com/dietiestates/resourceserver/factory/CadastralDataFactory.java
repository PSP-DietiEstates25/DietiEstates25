package com.dietiestates.resourceserver.factory;

import com.dietiestates.resourceserver.model.CadastralData;
import com.dietiestates.resourceserver.spec.CadastralDataSpec;

public interface CadastralDataFactory {
	
	CadastralData createCadastralDataFromSpec(
			CadastralDataSpec spec
			);
}
