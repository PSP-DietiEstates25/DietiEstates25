package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.factory.DetailFactory;
import com.dietiestates.api.model.Detail;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.spec.DetailSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DetailFactoryImpl implements DetailFactory {

	@Override
	public Detail createDetailFromSpec(
			DetailSpec spec,
			RealEstate realEstate,
			Search search
			) {
		return Detail.detailBuilder()
				.realEstate(realEstate)
				.search(search)
				.build();
	}
}
