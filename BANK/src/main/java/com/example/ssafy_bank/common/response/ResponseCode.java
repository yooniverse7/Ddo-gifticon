package com.example.ssafy_bank.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    ALREADY_EXIST_USER(400, HttpStatus.BAD_REQUEST, "이미 존재하는 계정입니다."),
    SUCCESS_SELECT_HISTORY(successCode(), HttpStatus.OK, "계좌 내역 조회 성공했습니다."),
    SUCCESS_SELECT_BALANCE(successCode(), HttpStatus.OK, "계좌 잔액 조회 성공했습니다."),
    SUCCESS_CREATE_USER_KEY(successCode(), HttpStatus.OK, "사용자 키와 계좌가 생성되었습니다."),
    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류"),
    USER_NOT_FOUND(403, HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."),
    SUCCESS_LOGIN(successCode(), HttpStatus.OK, "로그인 성공했습니다."),
    INSUFFICATION_BALANCE(403, HttpStatus.BAD_REQUEST, "계좌 잔액이 부족합니다."),
    SUCCESS_TRANSFER(successCode(), HttpStatus.OK, "계좌이체가 성공했습니다");

    private int code;
    private HttpStatus httpStatus;
    private String message;

    private static int successCode() {
        return 200;
    }
}
