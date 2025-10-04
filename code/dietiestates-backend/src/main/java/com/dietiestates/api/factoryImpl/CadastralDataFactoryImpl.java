package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.CadastralDataRequest;
import com.dietiestates.api.factory.CadastralDataFactory;
import com.dietiestates.api.model.CadastralData;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CadastralDataFactoryImpl implements CadastralDataFactory {

	@Override
	public CadastralData createCadastralData(CadastralDataRequest request) {
		return new CadastralData();
	}
}
