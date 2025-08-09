package com.dietiestates.api.dto;

public record LoginResponse (
    String token,
    String role,        // user, admin, agent
    String subjectType  // user, staffer
) {}
