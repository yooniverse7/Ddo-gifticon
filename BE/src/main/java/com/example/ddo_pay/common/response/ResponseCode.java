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

    // Restaurant
	SUCCESS_CREATE_RESTAURANT(204, HttpStatus.NO_CONTENT, "맛집 등록 성공."),
	SUCCESS_REMOVE_RESTAURANT(200, HttpStatus.OK, "맛집으로 등록된 가게를 해제했습니다."),
    SUCCESS_GET_RESTAURANT_LIST(200, HttpStatus.OK, "등록된 맛집 리스트 조회 성공."),
    SUCCESS_GET_SIMPLE_RESTAURANT(200, HttpStatus.OK, "간단한 맛집 정보 조회되었습니다."),
    SUCCESS_CREATE_CUSTOM_MENU(200, HttpStatus.OK, "커스텀 메뉴 등록에 성공했습니다."),
    SUCCESS_DELETE_CUSTOM_MENU(200, HttpStatus.OK, "커스텀 메뉴 삭제되었습니다."),
    SUCCESS_CRAWLING_RESTAURANT(200, HttpStatus.OK, "가게 상세 정보 조회(크롤링) 성공."),
    NO_EXIST_RESTAURANT(400, HttpStatus.BAD_REQUEST, "등록된 사용자가 아닙니다."),


    // 페이 도메인
    SUCCESS_BALANCE_CHECK(successCode(), HttpStatus.OK, "잔액조회가 성공적으로 완료되었습니다."),
    SUCCESS_POINT_CHECK(successCode(), HttpStatus.OK, "포인트조회가 성공적으로 완료되었습니다."),
    SUCCESS_DELETE_ACCOUNT(successCode(), HttpStatus.OK, "연결 계좌 삭제가 성공적으로 완료되었습니다."),
    SUCCESS_REGISTER_ACCOUNT(successCode(), HttpStatus.OK, "새로운 계좌 등록이 성공적으로 완료되었습니다."),
    SUCCESS_REFUND_GIFT(successCode(), HttpStatus.OK, "기프티콘 환불이 성공적으로 완료되었습니다."),
    SUCCESS_REGISTER_PASSWORD(successCode(), HttpStatus.OK, "비밀번호 등록이 성공적으로 완료되었습니다."),
    SUCCESS_BALANCE_CHARGE(successCode(), HttpStatus.OK, "또페이 충전이 성공적으로 완료되었습니다."),
    SUCCESS_ACCOUNT_CHECK(successCode(), HttpStatus.OK, "계좌조회가 성공적으로 완료되었습니다."),

    // 기프티콘
    SUCCESS_CREATE_GIFTICON(201, HttpStatus.CREATED, "기프티콘이 성공적으로 생성되었습니다."),
    SUCCESS_ASSIGNMENT_GIFTICON(200, HttpStatus.OK, "기프티콘이 성공적으로 양도되었습니다."),
    SUCCESS_LIST_GIFTICON(200, HttpStatus.OK, "받은 기프티콘 리스트가 조회되었습니다."),
    SUCCESS_DETAIL_GIFTICON(200, HttpStatus.OK, "기프티콘 상세 정보가 조회되었습니다."),
    SUCCESS_CHECK_GIFTICON(200, HttpStatus.OK, "기프티콘 사용여부가 조회되었습니다."),
    NO_EXIST_GIFTICON(400, HttpStatus.BAD_REQUEST, "등록된 기프티콘이 아닙니다."),
    NO_EXIST_GIFTBOX(400, HttpStatus.BAD_REQUEST, "받은 기프티콘이 아닙니다."),
    EXPIRED_GIFTICON(400, HttpStatus.BAD_REQUEST, "기프티콘의 유효기간이 만료되었습니다."),

    // enum 마지막. 복붙하는 과정에서 ,/; 차이에서 오는 충돌 이슈를 방지하기 위해 만들어놓음
    FINAL_FINAL(0, null, "enum 마지막 입니다. 쓰지 마세요");

    private int code;
    private HttpStatus httpStatus;
    private String message;

    private static int successCode() {
        return 200;
    }
}
