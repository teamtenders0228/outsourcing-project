package com.example.outsourcingproject.domain.user.exception;

import com.example.outsourcingproject.common.exception.BaseException;

import static com.example.outsourcingproject.common.exception.ErrorCode.USER_NOT_FOUND;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException() {
        super(USER_NOT_FOUND, USER_NOT_FOUND.getMessage());
    }
}
