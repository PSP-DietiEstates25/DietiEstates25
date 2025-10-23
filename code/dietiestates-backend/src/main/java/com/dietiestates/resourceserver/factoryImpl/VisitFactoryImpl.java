package com.dietiestates.resourceserver.factoryImpl;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.factory.VisitFactory;
import com.dietiestates.resourceserver.model.RealEstate;
import com.dietiestates.resourceserver.model.User;
import com.dietiestates.resourceserver.model.Visit;
import com.dietiestates.resourceserver.spec.VisitSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VisitFactoryImpl implements VisitFactory {

	@Override
	public Visit createVisitFromSpec(
			VisitSpec spec,
			User user,
			RealEstate realEstate
			) {
		return Visit.builder()
				.category(spec.getCategory())
				.status(spec.getStatus())
				.user(user)
				.realEstate(realEstate)
				.date(LocalDate.parse(spec.getDate()))
				.time(LocalTime.parse(spec.getTime()))
				.build();
	}

}
