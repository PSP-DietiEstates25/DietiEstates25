package com.dietiestates.resource_server.dto.response;

import com.dietiestates.resource_server.model.Admin;
import com.dietiestates.resource_server.model.EstateAgent;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

@Getter
@Setter
@ToString
@Builder
public class CreatedStaffersResponse {
    private Page<Admin> createdAdmins;
    private Page<EstateAgent> createdEstateAgents;
}
