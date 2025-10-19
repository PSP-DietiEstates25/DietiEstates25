package com.dietiestates.resourceserver.finder;

import com.dietiestates.resourceserver.exception.notfound.CadastralDataNotFoundException;
import com.dietiestates.resourceserver.model.CadastralData;

public interface CadastralDataFinder {

	CadastralData getCadastralDataById(Long id)
			throws CadastralDataNotFoundException;
	
}
