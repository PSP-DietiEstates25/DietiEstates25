package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.CadastralDataNotFoundException;
import com.dietiestates.resource_server.model.CadastralData;

public interface CadastralDataFinder {

	CadastralData getCadastralDataById(Long id)
			throws CadastralDataNotFoundException;
	
}
