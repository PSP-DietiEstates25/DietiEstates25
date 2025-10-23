package com.dietiestates.resourceserver.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.factory.SearchFactory;
import com.dietiestates.resourceserver.model.CadastralFilter;
import com.dietiestates.resourceserver.model.Detail;
import com.dietiestates.resourceserver.model.Search;
import com.dietiestates.resourceserver.model.User;
import com.dietiestates.resourceserver.spec.SearchSpec;

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
