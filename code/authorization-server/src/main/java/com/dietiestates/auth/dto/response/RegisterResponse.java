package com.dietiestates.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class RegisterResponse {

    private Long id;
    private String email;
    private String role;
    private Boolean enabled;
    private Boolean locked;
}