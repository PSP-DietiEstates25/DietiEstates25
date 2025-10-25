package com.dietiestates.auth.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleName {
    USER,
    ADMIN,
    ESTATE_AGENT
    ;

    @JsonValue
    public String toValue() {
        return this.name();
    }

    @JsonCreator
    public static RoleName fromValue(String value) {
        return valueOf(value);
    }
}