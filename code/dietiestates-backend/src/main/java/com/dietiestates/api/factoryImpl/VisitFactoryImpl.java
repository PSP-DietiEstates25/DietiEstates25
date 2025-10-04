package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.enums.ProposalCategory;
import com.dietiestates.api.enums.ProposalStatus;
import com.dietiestates.api.factory.VisitFactory;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.User;
import com.dietiestates.api.model.Visit;
import com.dietiestates.api.spec.VisitSpec;

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
		return Visit.visitBuilder()
				.category(ProposalCategory.valueOf(spec.getCategory()))
				.status(ProposalStatus.valueOf(spec.getStatus()))
				.user(user)
				.realEstate(realEstate)
				.date(spec.getDate())
				.time(spec.getTime())
				.build();
	}

}
