package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.UtilityRequest;
import com.dietiestates.api.factory.UtilityFactory;
import com.dietiestates.api.model.Utility;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UtilityFactoryImpl implements UtilityFactory {

	@Override
	public Utility createUtility(UtilityRequest request, Long detailId) {
		// TODO Auto-generated method stub
		return null;
	}

}
