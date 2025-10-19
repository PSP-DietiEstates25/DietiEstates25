package com.dietiestates.resourceserver.factory;

import com.dietiestates.resourceserver.model.CadastralFilter;
import com.dietiestates.resourceserver.spec.CadastralFilterSpec;

public interface CadastralFilterFactory {

	CadastralFilter createCadastralFilterFromSpec(
			CadastralFilterSpec spec
			);
}
