package com.example.outsourcingproject.domain.user.enums;

import java.util.Arrays;

public enum UserRole {
    USER, OWNER;

    public static UserRole of(String role){
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Role"));
    }
}
