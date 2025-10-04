package com.dietiestates.api.finder;

import com.dietiestates.api.exception.notfound.CadastralFilterNotFoundException;
import com.dietiestates.api.model.CadastralFilter;

public interface CadastralFilterFinder {

	CadastralFilter getCadastralFilterById(Long id)
			throws CadastralFilterNotFoundException;
}
