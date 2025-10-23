package com.dietiestates.resourceserver.finder;

import com.dietiestates.resourceserver.exception.notfound.CadastralFilterNotFoundException;
import com.dietiestates.resourceserver.model.CadastralFilter;

public interface CadastralFilterFinder {

	CadastralFilter getCadastralFilterById(Long id)
			throws CadastralFilterNotFoundException;
}
