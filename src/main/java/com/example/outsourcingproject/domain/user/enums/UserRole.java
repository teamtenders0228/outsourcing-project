package com.example.outsourcingproject.domain.user.enums;

import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;

import java.util.Arrays;

public enum UserRole {
    USER,
    OWNER,
    ADMIN;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new BaseException(ErrorCode.INVALID_USER_ROLE, null));
    }
}
