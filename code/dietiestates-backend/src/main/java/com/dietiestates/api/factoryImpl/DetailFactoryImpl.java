package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.DetailRequest;
import com.dietiestates.api.factory.DetailFactory;
import com.dietiestates.api.model.Detail;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DetailFactoryImpl implements DetailFactory {

	@Override
	public Detail createDetail(DetailRequest request) {
		return new Detail();
	}
}
