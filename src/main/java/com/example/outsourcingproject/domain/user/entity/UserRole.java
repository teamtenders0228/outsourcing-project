package com.example.outsourcingproject.domain.user.entity;

import com.example.outsourcingproject.domain.auth.exception.InvalidUserRoleException;

import java.util.Arrays;

public enum UserRole {
    USER,
    OWNER,
    ADMIN;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new InvalidUserRoleException());
    }
}
