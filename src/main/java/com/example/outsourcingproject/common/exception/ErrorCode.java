package com.example.outsourcingproject.common.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //유저 관련 에러 코드
    DUPLICATE_EMAIL("중복된 이메일이 있습니다.", HttpStatus.BAD_REQUEST),
    INVALID_USER_ROLE("유효하지 않은 역할입니다.", HttpStatus.BAD_REQUEST),
    INVALID_FORM("유효하지 않은 형식입니다.", HttpStatus.BAD_REQUEST),
    USER_NOT_EXIST("존재하지 않는 회원입니다.", HttpStatus.BAD_REQUEST),
    SIGNIN_FAILED("로그인에 실패했습니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_MISMATCH("잘못된 비밀번호입니다.", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND("사용자 정보를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_SAME_AS_OLD("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.", HttpStatus.BAD_REQUEST),
    INACTIVE_USER("이미 탈퇴된 회원입니다.", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_NOT_FOUND("해당 refresh token을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_REFRESH_TOKEN("유효하지 않은 refresh token 입니다.", HttpStatus.BAD_REQUEST),
    //가게 관련 에러 코드
    INVALID_CATEGORY_TYPE("유효하지 않은 카테고리 이름입니다.", HttpStatus.BAD_REQUEST),
    CONFLICT_STORE_NAME("동일한 가게이름이 존재합니다.",HttpStatus.CONFLICT),
    EXCEED_STORE_LIMIT("최대 3개 까지의 가게를 등록할 수 있습니다.",HttpStatus.BAD_REQUEST),
    STORE_NOT_FOUND("가게 정보를 찾을 수 없습니다.",HttpStatus.NOT_FOUND),
    UNAUTHORIZED_STORE_ACCESS("본인의 가게가 아닙니다.",HttpStatus.BAD_REQUEST),
    //주문 관련 에러 코드
    //리뷰 관련 에러 코드
    //그 외 에러 코드
    INVALID_TYPE("유효하지 않은 타입입니다.",HttpStatus.BAD_REQUEST),
    SERVER_NOT_WORK("서버 문제로 인해 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus httpStatus;
}
