package com.dietiestates.api.factory;

import com.dietiestates.api.model.CadastralFilter;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.spec.CadastralFilterSpec;

public interface CadastralFilterFactory {

	CadastralFilter createCadastralFilterFromSpec(
			CadastralFilterSpec spec,
			Search search
			);
}
