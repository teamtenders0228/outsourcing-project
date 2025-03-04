package com.example.outsourcingproject.common.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //유저 관련 에러 코드
    //가게 관련 에러 코드
    INVALID_CATEGORY_TYPE("유효하지 않은 카테고리 이름입니다.", HttpStatus.BAD_REQUEST),
    CONFLICT_STORE_NAME("동일한 가게이름이 존재합니다.",HttpStatus.CONFLICT),
    //주문 관련 에러 코드
    //리뷰 관련 에러 코드
    //그 외 에러 코드
    INVALID_TYPE("유효하지 않은 타입입니다.",HttpStatus.BAD_REQUEST),
    SERVER_NOT_WORK("서버 문제로 인해 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus httpStatus;
}
