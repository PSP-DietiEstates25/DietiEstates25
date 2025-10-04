package com.dietiestates.api.finder;

import com.dietiestates.api.exception.notfound.CadastralDataNotFoundException;
import com.dietiestates.api.model.CadastralData;

public interface CadastralDataFinder {

	CadastralData getCadastralDataById(Long id)
			throws CadastralDataNotFoundException;
	
}
