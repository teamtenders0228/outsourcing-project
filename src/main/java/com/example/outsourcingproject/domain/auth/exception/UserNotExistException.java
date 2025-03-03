package com.example.outsourcingproject.domain.auth.exception;

import com.example.outsourcingproject.common.exception.BaseException;

import static com.example.outsourcingproject.common.exception.ErrorCode.USER_NOT_EXIST;

public class UserNotExistException extends BaseException {
    public UserNotExistException() {
        super(USER_NOT_EXIST, USER_NOT_EXIST.getMessage());
    }
}
