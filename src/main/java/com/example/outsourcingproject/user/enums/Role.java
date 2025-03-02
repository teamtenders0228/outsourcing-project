package com.example.outsourcingproject.user.enums;

import com.sun.jdi.request.InvalidRequestStateException;

import java.util.Arrays;

public enum Role {
    USER, OWNER;

    public static Role of(String role){
        return Arrays.stream(Role.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Role"));
    }
}
