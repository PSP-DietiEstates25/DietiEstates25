package com.dietiestates.resource_server.factorydefaultimpl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dietiestates.resource_server.factory.SearchRealEstateFactory;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.Search;
import com.dietiestates.resource_server.model.SearchRealEstate;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SearchRealEstateFactoryDefaultImpl implements SearchRealEstateFactory {

    @Override
    public SearchRealEstate createSearchRealEstate(Search search, RealEstate realEstate) {
        return SearchRealEstate.builder()
                .realEstate(realEstate)
                .search(search)
                .build();
    }
}
