package com.example.outsourcingproject.domain.user.exception;

import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;

import static com.example.outsourcingproject.common.exception.ErrorCode.INACTIVE_USER;

public class InactiveUserException extends BaseException {

    public InactiveUserException() {
        super(INACTIVE_USER, INACTIVE_USER.getMessage());
    }
}
