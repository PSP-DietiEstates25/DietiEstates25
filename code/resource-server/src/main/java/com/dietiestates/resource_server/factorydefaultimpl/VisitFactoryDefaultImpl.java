package com.dietiestates.resource_server.factorydefaultimpl;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.stereotype.Component;

import com.dietiestates.resource_server.factory.VisitFactory;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.User;
import com.dietiestates.resource_server.model.Visit;
import com.dietiestates.resource_server.spec.VisitSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VisitFactoryDefaultImpl implements VisitFactory {

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
