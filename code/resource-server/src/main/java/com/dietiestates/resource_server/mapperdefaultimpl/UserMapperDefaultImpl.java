package com.dietiestates.resource_server.mapperdefaultimpl;

import com.dietiestates.resource_server.dto.request.UserRequest;
import com.dietiestates.resource_server.dto.response.UserResponse;
import com.dietiestates.resource_server.mapper.UserMapper;
import com.dietiestates.resource_server.model.User;
import com.dietiestates.resource_server.spec.UserSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapperDefaultImpl implements UserMapper {

    @Override
    public UserSpec toSpec(UserRequest request) {
        return UserSpec.builder()
                .email(request.getEmail())
                .build();
    }

    @Override
    public UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .createdDate(user.getCreatedDate())
                .lastModifiedDate(user.getLastModifiedDate())
                .notifications(user.getNotifications())
                .searches(user.getSearches())
                .proposals(user.getProposals())
                .build();
    }
}
