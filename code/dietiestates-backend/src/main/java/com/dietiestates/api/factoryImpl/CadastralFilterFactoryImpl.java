package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.CadastralFilterRequest;
import com.dietiestates.api.factory.CadastralFilterFactory;
import com.dietiestates.api.model.CadastralFilter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CadastralFilterFactoryImpl implements CadastralFilterFactory {

	@Override
	public CadastralFilter createCadastralFilter(CadastralFilterRequest request, Long searchId) {
		return new CadastralFilter();
	}
}
