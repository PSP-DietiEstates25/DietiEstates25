package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.VisitRequest;
import com.dietiestates.api.factory.VisitFactory;
import com.dietiestates.api.model.Visit;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VisitFactoryImpl implements VisitFactory {

	@Override
	public Visit createVisit(VisitRequest request, Long realEstateId) {
		// TODO Auto-generated method stub
		return null;
	}

}
