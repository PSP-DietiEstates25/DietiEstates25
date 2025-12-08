package com.dietiestates.resource_server.utils;

import com.dietiestates.resource_server.enums.ProposalStatus;
import com.dietiestates.resource_server.exception.notfound.ProposalStatusNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProposalUtils {

    public static ProposalStatus extractProposalStatus(String status){
        try {
            return ProposalStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ProposalStatusNotFoundException();
        }
    }

    public static boolean checkProposalStatusExists(String status){
        return status != null && !status.isEmpty();
    }
}
