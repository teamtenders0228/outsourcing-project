package com.example.outsourcingproject.domain.auth.exception;

import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;

import static com.example.outsourcingproject.common.exception.ErrorCode.PASSWORD_MISMATCH;

public class PasswordMismatchException extends BaseException {
    public PasswordMismatchException() {
        super(PASSWORD_MISMATCH, PASSWORD_MISMATCH.getMessage());
    }
}
