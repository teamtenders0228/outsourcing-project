package com.example.outsourcingproject.domain.user.exception;

import com.example.outsourcingproject.common.exception.BaseException;

import static com.example.outsourcingproject.common.exception.ErrorCode.PASSWORD_SAME_AS_OLD;

public class DuplicatePasswordException extends BaseException {
    public DuplicatePasswordException() {
        super(PASSWORD_SAME_AS_OLD, PASSWORD_SAME_AS_OLD.getMessage());
    }
}
