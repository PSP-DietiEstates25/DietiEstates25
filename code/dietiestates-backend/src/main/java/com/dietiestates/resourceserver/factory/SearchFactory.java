package com.dietiestates.resourceserver.factory;

import com.dietiestates.resourceserver.model.CadastralFilter;
import com.dietiestates.resourceserver.model.Detail;
import com.dietiestates.resourceserver.model.Search;
import com.dietiestates.resourceserver.model.User;
import com.dietiestates.resourceserver.spec.SearchSpec;

public interface SearchFactory {

	Search createSearchFromSpec(
			SearchSpec spec,
			User user,
			CadastralFilter cadastralFilter,
			Detail detail
			);
}
