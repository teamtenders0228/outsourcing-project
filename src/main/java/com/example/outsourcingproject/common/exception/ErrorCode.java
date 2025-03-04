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
    USER_NOT_FOUND("사용자 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PASSWORD_SAME_AS_OLD("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.", HttpStatus.BAD_REQUEST),
    INACTIVE_USER("이미 탈퇴된 회원입니다.", HttpStatus.BAD_REQUEST),
    //가게 관련 에러 코드
    STORE_NOT_FOUND("가게 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    //주문 관련 에러 코드
    ORDER_ONLY_FOR_REGULAR_USER("주문은 일반 회원만 이용할 수 있습니다.", HttpStatus.UNAUTHORIZED),
    ORDER_ACCEPT_ONLY_FOR_OWNER("주문 수락은 사장님만 가능합니다.", HttpStatus.UNAUTHORIZED),
    ORDER_REJECT_ONLY_FOR_OWNER("주문 거절은 사장님만 가능합니다.", HttpStatus.UNAUTHORIZED),
    ORDER_STATUS_ONLY_FOR_OWNER("주문 상태 변경은 사장님만 가능합니다.", HttpStatus.UNAUTHORIZED),
    ORDER_NOT_FOUND("주문 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    // 메뉴 관련 에러 코드
    MENU_NOT_FOUND("메뉴 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    //리뷰 관련 에러 코드
    //그 외 에러 코드
    INVALID_TYPE("유효하지 않은 타입입니다.",HttpStatus.BAD_REQUEST),
    SERVER_NOT_WORK("서버 문제로 인해 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus httpStatus;
}
