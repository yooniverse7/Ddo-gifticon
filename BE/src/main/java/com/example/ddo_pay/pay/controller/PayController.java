package com.example.ddo_pay.pay.controller;

import com.example.ddo_pay.common.response.Response;
import com.example.ddo_pay.common.response.ResponseCode;
import com.example.ddo_pay.common.util.SecurityUtil;
import com.example.ddo_pay.pay.dto.request.AccountVerifyRequest;
import com.example.ddo_pay.pay.dto.request.ChargeDdoPayRequest;
import com.example.ddo_pay.pay.dto.request.RegisterAccountRequest;
import com.example.ddo_pay.pay.dto.request.RegisterPasswordRequest;
import com.example.ddo_pay.pay.dto.response.GetAccountResponse;
import com.example.ddo_pay.pay.dto.response.GetBalanceResponse;
import com.example.ddo_pay.pay.dto.response.GetPointResponse;
import com.example.ddo_pay.pay.service.PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/pay")
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    // 유효 계좌 인증
    @PostMapping("/account/verify")
    public ResponseEntity<?> verifyAccount(@RequestBody AccountVerifyRequest request) {
        Long userId = SecurityUtil.getUserId();
        String financeCode = payService.verifyAccount(userId, request);

        ResponseCode responseCode = switch (financeCode) {
            case "H0000" -> ResponseCode.SUCCESS_VERIFY_ACCOUNT;
            case "A1003" -> ResponseCode.INVALID_ACCOUNT;
            case "ERR_API" -> ResponseCode.FINANCE_API_ERROR;
            case "ERR_PARSING" -> ResponseCode.FINANCE_PARSING_ERROR;
            default -> ResponseCode.UNKNOWN_ERROR;
        };

        return new ResponseEntity<>(Response.create(responseCode, null), responseCode.getHttpStatus());
    }

    // 비밀번호 등록 및 또페이 생성
    @PostMapping("/password")
    public ResponseEntity<?> registerPassword(@RequestBody RegisterPasswordRequest request) {
        Long userId = SecurityUtil.getUserId();
        payService.registerPayPassword(userId, request);
        return ResponseEntity.ok(Response.create(ResponseCode.SUCCESS_REGISTER_DDOPAY, null));
    }

    // 본인 계좌 인증 후 계좌 연동
    @PostMapping("/account")
    public ResponseEntity<?> registerAccount(@RequestBody RegisterAccountRequest request) {
        Long userId = SecurityUtil.getUserId();
        payService.registerAccount(userId, request);

        return ResponseEntity.ok(
                Response.create(ResponseCode.SUCCESS_REGISTER_ACCOUNT, null)
        );
    }


    // 잔고 조회
    @GetMapping("/balance")
    public ResponseEntity<?> selectBalance() {
        Long userId = SecurityUtil.getUserId();
        GetBalanceResponse response = payService.selectBalance(userId);
        return ResponseEntity.ok(Response.create(ResponseCode.SUCCESS_BALANCE_CHECK, response));
    }


    // 포인트 조회
    @GetMapping("/point")
    public ResponseEntity<?> selectPoint() {
        Long userId = SecurityUtil.getUserId();
        GetPointResponse response = payService.selectPoint(userId);
        return ResponseEntity.ok(Response.create(ResponseCode.SUCCESS_POINT_CHECK, response));
    }

    // 연결된 계좌 조회
    @GetMapping("/account")
    public ResponseEntity<?> selectAccountList() {
        Long userId = SecurityUtil.getUserId();
        List<GetAccountResponse> dtos = payService.selectAccountList(userId);
        return ResponseEntity.ok(Response.create(ResponseCode.SUCCESS_ACCOUNT_CHECK, dtos));
    }

    // 또페이 충전
    @PostMapping("/charge")
    public ResponseEntity<?> chargeDdoPay(@RequestBody ChargeDdoPayRequest request) {
        Long userId = SecurityUtil.getUserId();
        payService.transferDdoPay(userId, request);
        return ResponseEntity.ok(Response.create(ResponseCode.SUCCESS_BALANCE_CHARGE, "충전성공"));
    }

    // POS에서 토큰, 결제 금액, 가게 계좌 받기
    @PostMapping("/pos")
    public ResponseEntity<?> posPayment() {
        return null;
    }



}
