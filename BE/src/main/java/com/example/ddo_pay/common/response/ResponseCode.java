package com.example.ddo_pay.common.response;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    // User 도메인
    // 소셜 로그인



    SUCCESS_SOCIAL_LOGIN(200, HttpStatus.OK, "인가 코드를 통해 액세스 토큰 요청이 성공했습니다."),
    SUCCESS_LOGIN(successCode(), HttpStatus.OK, "로그인이 성공적으로 완료되었습니다."),


    // 회원 정보 불러오기
    SUCCESS_GET_USER_INFO(successCode(), HttpStatus.OK, "회원 정보 조회 성공"),


    // 회원 정보 수정
    SUCCESS_UPDATE_USER_INFO(204, HttpStatus.OK, "회원 정보 수정 성공"),

    // 로그아웃
    SUCCESS_LOGOUT(204, HttpStatus.OK, "로그아웃 성공"),

    // User
    NO_EXIST_USER(400, HttpStatus.BAD_REQUEST, "등록된 사용자가 아닙니다."),
    INVALID_GIFTICON_OWNER(400, HttpStatus.BAD_REQUEST, "기프티콘 소유자가 아닙니다."),
    // Restaurant
	SUCCESS_CREATE_RESTAURANT(204, HttpStatus.NO_CONTENT, "맛집 등록 성공."),
	SUCCESS_REMOVE_RESTAURANT(200, HttpStatus.OK, "맛집으로 등록된 가게를 해제했습니다."),
    SUCCESS_GET_RESTAURANT_LIST(200, HttpStatus.OK, "등록된 맛집 리스트 조회 성공."),
    SUCCESS_GET_SIMPLE_RESTAURANT(200, HttpStatus.OK, "간단한 맛집 정보 조회되었습니다."),
    SUCCESS_CREATE_CUSTOM_MENU(200, HttpStatus.OK, "커스텀 메뉴 등록에 성공했습니다."),
    SUCCESS_DELETE_CUSTOM_MENU(200, HttpStatus.OK, "커스텀 메뉴 삭제되었습니다."),
    SUCCESS_CRAWLING_RESTAURANT(200, HttpStatus.OK, "가게 상세 정보 조회(크롤링) 성공."),
    NO_EXIST_RESTAURANT(400, HttpStatus.BAD_REQUEST, "등록된 사용자가 아닙니다."),
    DATA_ALREADY_EXISTS(409, HttpStatus.CONFLICT, "이미 데이터가 존재합니다."),
    NO_EXIST_CUSTOM_MENU(400, HttpStatus.BAD_REQUEST, "해당 커스텀 메뉴가 존재하지 않습니다."),
    ALREADY_EXIST_RESTAURANT(400, HttpStatus.BAD_REQUEST, "이미 맛집으로 등록한 식당입니다."),


    // 페이 도메인
    SUCCESS_BALANCE_CHECK(successCode(), HttpStatus.OK, "잔액조회가 성공적으로 완료되었습니다."),
    SUCCESS_POINT_CHECK(successCode(), HttpStatus.OK, "포인트조회가 성공적으로 완료되었습니다."),
    SUCCESS_DELETE_ACCOUNT(successCode(), HttpStatus.OK, "연결 계좌 삭제가 성공적으로 완료되었습니다."),
    SUCCESS_REGISTER_ACCOUNT(successCode(), HttpStatus.OK, "새로운 계좌 등록이 성공적으로 완료되었습니다."),
    SUCCESS_REFUND_GIFT(successCode(), HttpStatus.OK, "기프티콘 환불이 성공적으로 완료되었습니다."),
    SUCCESS_REGISTER_PASSWORD(successCode(), HttpStatus.OK, "비밀번호 등록이 성공적으로 완료되었습니다."),
    SUCCESS_BALANCE_CHARGE(successCode(), HttpStatus.OK, "또페이 충전이 성공적으로 완료되었습니다."),
    SUCCESS_ACCOUNT_CHECK(successCode(), HttpStatus.OK, "계좌조회가 성공적으로 완료되었습니다."),
    SUCCESS_VERIFY_ACCOUNT(successCode(), HttpStatus.OK, "연결 가능한 계좌입니다."),
    NO_EXIST_ACCOUNT(400, HttpStatus.OK, "계좌가 존재하지 않습니다."),
    INVALID_ACCOUNT(1003, HttpStatus.OK, "유효하지 않은 계좌입니다."),
    FINANCE_API_ERROR(1500, HttpStatus.INTERNAL_SERVER_ERROR, "금융망 요청 실패"),
    FINANCE_PARSING_ERROR(1501, HttpStatus.INTERNAL_SERVER_ERROR, "금융망 응답 파싱 중 오류가 발생했습니다."),
    UNKNOWN_ERROR(1999, HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다."),
    SUCCESS_REGISTER_DDOPAY(successCode(), HttpStatus.OK, "또페이 생성 및 비밀번호 등록이 완료되었습니다."),
    INVALID_PAY_PASSWORD(3000, HttpStatus.INTERNAL_SERVER_ERROR, "비밀번호의 형식이 올바르지 않습니다."),
    ALREADY_REGISTERED_DDOPAY(1004, HttpStatus.BAD_REQUEST, "이미 또페이가 등록되어 있습니다."),
    REDIS_NOT_FOUND(400, HttpStatus.BAD_REQUEST, "Redis에 값이 존재하지 않습니다."),
    INVALID_REDIS_FORMAT(1004, HttpStatus.BAD_REQUEST, "Redis에 저장된 형식이 올바르지 않습니다."),
    NOT_VERIFIED_ACCOUNT(1005, HttpStatus.BAD_REQUEST, "계좌 인증이 완료되지 않았습니다."),
    NO_EXIST_DDOPAY(1500, HttpStatus.BAD_REQUEST, "연결된 또페이를 찾을 수 없습니다."),
    INSUFFICIENT_BALANCE(4001, HttpStatus.BAD_REQUEST, "잔액이 부족합니다."),
    DIFFRENT_PASSWORD(1500, HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다"),
    INTERNAL_SERVER_ERROR(1999, HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),
    INVALID_GIFT_PASSWORD(3000, HttpStatus.BAD_REQUEST, "기프티콘 비밀번호가 일치하지 않습니다."),

    // 기프티콘
    SUCCESS_CREATE_GIFTICON(201, HttpStatus.CREATED, "기프티콘이 성공적으로 생성되었습니다."),
    SUCCESS_ASSIGNMENT_GIFTICON(200, HttpStatus.OK, "기프티콘이 성공적으로 양도되었습니다."),
    SUCCESS_LIST_GIFTICON(200, HttpStatus.OK, "받은 기프티콘 리스트가 조회되었습니다."),
    SUCCESS_DETAIL_GIFTICON(200, HttpStatus.OK, "기프티콘 상세 정보가 조회되었습니다."),
    SUCCESS_CHECK_GIFTICON(200, HttpStatus.OK, "기프티콘 사용여부가 조회되었습니다."),
    NO_EXIST_GIFTICON(400, HttpStatus.BAD_REQUEST, "등록된 기프티콘이 아닙니다."),
    NO_EXIST_GIFTBOX(400, HttpStatus.BAD_REQUEST, "받은 기프티콘이 아닙니다."),
    EXPIRED_GIFTICON(400, HttpStatus.BAD_REQUEST, "기프티콘의 유효기간이 만료되었습니다."),
    NOT_REFUNDABLE_GIFTICON(400, HttpStatus.BAD_REQUEST, "환불할 수 없는 기프티콘 입니다."),
    GIFT_NOT_USABLE(400, HttpStatus.BAD_REQUEST, "기프티콘 사용이 불가능합니다.");
    private int code;
    private HttpStatus httpStatus;
    private String message;

    private static int successCode() {
        return 200;
    }
}
