package com.dietiestates.api.factory;

import com.dietiestates.api.dto.request.CadastralFilterRequest;
import com.dietiestates.api.model.CadastralFilter;

public interface CadastralFilterFactory {

	CadastralFilter createCadastralFilter(CadastralFilterRequest request, Long searchId);
}
