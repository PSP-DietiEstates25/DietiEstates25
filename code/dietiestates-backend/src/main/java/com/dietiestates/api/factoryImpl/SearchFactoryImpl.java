package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.factory.SearchFactory;
import com.dietiestates.api.model.CadastralFilter;
import com.dietiestates.api.model.Detail;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.model.User;
import com.dietiestates.api.spec.SearchSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SearchFactoryImpl implements SearchFactory {

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
