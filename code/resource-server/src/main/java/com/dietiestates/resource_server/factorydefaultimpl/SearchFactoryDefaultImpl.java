package com.dietiestates.resource_server.factorydefaultimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resource_server.factory.SearchFactory;
import com.dietiestates.resource_server.model.CadastralFilter;
import com.dietiestates.resource_server.model.Detail;
import com.dietiestates.resource_server.model.Search;
import com.dietiestates.resource_server.model.User;
import com.dietiestates.resource_server.spec.SearchSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SearchFactoryDefaultImpl implements SearchFactory {

    @Override
    public Search createSearchFromSpec(
            SearchSpec spec,
            User user,
            CadastralFilter cadastralFilter,
            Detail detail
    ) {
        return Search.builder()
                .category(spec.getCategory())
                .size(spec.getSize())
                .page(spec.getPage() - 1)
                .user(user)
                .cadastralFilter(cadastralFilter)
                .detail(detail)
                .build();
    }

}
