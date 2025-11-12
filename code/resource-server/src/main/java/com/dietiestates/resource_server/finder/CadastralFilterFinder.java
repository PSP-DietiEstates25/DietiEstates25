package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.CadastralFilterNotFoundException;
import com.dietiestates.resource_server.model.CadastralFilter;

public interface CadastralFilterFinder {
	CadastralFilter getCadastralFilterById(Long id) throws CadastralFilterNotFoundException;
    CadastralFilter getSearchCadastralFilter(Long searchId) throws CadastralFilterNotFoundException;
}
