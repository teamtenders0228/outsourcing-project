package com.example.outsourcingproject.domain.auth.exception;

import com.example.outsourcingproject.common.exception.BaseException;

import static com.example.outsourcingproject.common.exception.ErrorCode.DUPLICATE_EMAIL;

public class DuplicateEmailException extends BaseException {
  public DuplicateEmailException() {
    super(DUPLICATE_EMAIL, DUPLICATE_EMAIL.getMessage());
  }
}
