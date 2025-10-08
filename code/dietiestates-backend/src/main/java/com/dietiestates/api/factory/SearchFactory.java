package com.dietiestates.api.factory;

import com.dietiestates.api.model.CadastralFilter;
import com.dietiestates.api.model.Detail;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.model.User;
import com.dietiestates.api.spec.SearchSpec;

public interface SearchFactory {

	Search createSearchFromSpec(
			SearchSpec spec,
			User user,
			CadastralFilter cadastralFilter,
			Detail detail
			);
}
