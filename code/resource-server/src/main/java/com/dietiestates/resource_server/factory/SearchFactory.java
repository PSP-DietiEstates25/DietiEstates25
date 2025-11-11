package com.dietiestates.resource_server.factory;

import com.dietiestates.resource_server.model.CadastralFilter;
import com.dietiestates.resource_server.model.Detail;
import com.dietiestates.resource_server.model.Search;
import com.dietiestates.resource_server.model.User;
import com.dietiestates.resource_server.spec.SearchSpec;

public interface SearchFactory {
    Search createSearchFromSpec(SearchSpec spec, User user, CadastralFilter cadastralFilter, Detail detail);
}
