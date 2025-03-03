package com.example.outsourcingproject.domain.auth.exception;

import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;

public class InvalidUserRoleException extends BaseException {
    public InvalidUserRoleException() {
        super(ErrorCode.INVALID_USER_ROLE, "유효하지 않은 역할입니다.");
    }
}
