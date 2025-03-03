package com.example.outsourcingproject.domain.auth.exception;

import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;

public class InvalidFormException extends BaseException {
    public InvalidFormException(String message) {
        super(ErrorCode.INVALID_FORM, message);
    }
}
